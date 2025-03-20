package uni.capstone.moodmingle.clients.llm.gpt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;
import uni.capstone.moodmingle.clients.llm.gpt.circuitbreaker.GptCircuitBreaker;
import uni.capstone.moodmingle.clients.llm.gpt.dto.GptMessage;
import uni.capstone.moodmingle.clients.llm.gpt.dto.GptResponseInfo;
import uni.capstone.moodmingle.clients.llm.gpt.facade.PromptProcessingFacade;
import uni.capstone.moodmingle.clients.llm.gpt.log.FailedLog;
import uni.capstone.moodmingle.clients.llm.gpt.log.FailedLogRepository;
import uni.capstone.moodmingle.domain.diary.application.ReplyHandlerService;
import uni.capstone.moodmingle.domain.diary.application.dto.request.ReplyCreateCommand;
import uni.capstone.moodmingle.domain.diary.domain.Reply;
import uni.capstone.moodmingle.global.error.ErrorCode;
import uni.capstone.moodmingle.global.error.exception.ExternalApiException;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 비동기 작업을 위해 WebClient 를 이용해서 GPT 와 통신하는 컴포넌트
 *
 * @author ijin
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GptClient {

    // OpenAI API 설정값
    @Value("${openai.api.url}")
    private String gptRequestUrl;
    @Value("${openai.api.model}")
    private String gptApiModel;

    private final WebClient gptWebClient;
    private final GptCircuitBreaker gptCircuitBreaker;
    private final FailedLogRepository failedLogRepository;
    private final ReplyHandlerService handler;
    private final PromptProcessingFacade processingFacade;

    // 위로 요청
    public void requestConsoleLetter(ReplyCreateCommand command, Long diaryId) {
        List<GptMessage> prompts = processingFacade.processLetterReplyPrompt(command);
        requestToGptApi(gptApiModel, prompts, diaryId, Reply.Type.LETTER);
    }

    // 공감 요청
    public void requestSympathyPhrase(ReplyCreateCommand command, Long diaryId) {
        List<GptMessage> prompts = processingFacade.processSympathyReplyPrompt(command);
        requestToGptApi(gptApiModel, prompts, diaryId, Reply.Type.SYMPATHY);
    }

    // 충고 요청
    public void requestAdvicePhrase(ReplyCreateCommand command, Long diaryId) {
        List<GptMessage> prompts = processingFacade.processAdviceReplyPrompt(command);
        requestToGptApi(gptApiModel, prompts, diaryId, Reply.Type.ADVICE);
    }

    /**
     * GPT 와 통신하여 요청하는 메서드
     *
     * @param model    사용할 GPT Model
     * @param messages Prompt Messages
     * @param type 답장 Type
     */
    public void requestToGptApi(String model, List<GptMessage> messages, Long diaryId, Reply.Type type) {
        // 서킷 브레이커 상태 확인
        if (gptCircuitBreaker.checkCircuitBreakerOpened()) {
            throw new ExternalApiException(ErrorCode.CIRCUIT_BREAKER_OPENED);
        }

        // OpenAI API 요청
        gptWebClient
                .post()
                .uri(gptRequestUrl)
                .bodyValue(processOpenAIRequestBody(model, messages))
                .retrieve()
                .bodyToMono(GptResponseInfo.class)
                .doOnEach(signal -> {
                    putRequestId();                             // OpenAi API 응답 처리하는 스레드에 식별자값 부여
                })
                .doOnError(error -> {
                    logAndSaveErrorMessages(diaryId, error);    // 에러메세지 로그 및 저장
                })
                .map(gptResponse -> gptResponse.getChoices().stream()
                        .map(choice -> choice.getMessage().getContent())
                        .toList()
                        .get(0))                                // OpenAi API 응답 가공
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                        .jitter(0.5))                // 랜덤 백오프 3회 재시도 정책
                .doFinally(signal -> MDC.clear())              // 식별자 메모리에서 비우기
                .subscribe(
                        gptResponse -> handler.createAndSaveReply(diaryId, gptResponse, type),
                        error -> {
                            handler.treatFailedReplyDiary(diaryId);
                            gptCircuitBreaker.addFailureCount();        // 실패 횟수 추가
                        }
                );
    }

    // 스레드에 식별자값 부여
    private void putRequestId() {
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put("requestId", requestId);
    }

    // OpenAI API 통신 실패했을 때
    private void logAndSaveErrorMessages(Long diaryId, Throwable error) {
        log.error("❌ OpenAI API 오류 응답: " + error.getMessage());
        createAndSaveFailedLog(error.getMessage(), diaryId);
    }

    // 실패 로그 생성 및 저장
    private void createAndSaveFailedLog(String log, Long diaryId) {
        FailedLog failedLog = FailedLog.builder()
                .threadId((String) MDC.get("requestId"))
                .errorLog(log)
                .diaryId(diaryId)
                .build();
        failedLogRepository.save(failedLog);
    }

    // OpenAI API 요청 데이터 가공
    private Map<String, Object> processOpenAIRequestBody(String model, List<GptMessage> messages) {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("model", model);
        bodyMap.put("stream", false);
        bodyMap.put("messages", messages);
        bodyMap.put("temperature", 1.0);
        return bodyMap;
    }
}
