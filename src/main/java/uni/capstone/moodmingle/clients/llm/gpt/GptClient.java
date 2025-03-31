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
import uni.capstone.moodmingle.clients.llm.prompt.PromptProcessor;
import uni.capstone.moodmingle.clients.llm.gpt.log.LogHandlerService;
import uni.capstone.moodmingle.domain.diary.application.ReplyHandlerService;
import uni.capstone.moodmingle.domain.diary.application.dto.request.ReplyCreateCommand;
import uni.capstone.moodmingle.domain.diary.domain.Reply;
import uni.capstone.moodmingle.global.error.ErrorCode;
import uni.capstone.moodmingle.global.error.exception.ExternalApiException;

import java.time.Duration;
import java.util.*;

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
    private final LogHandlerService logHandler;
    private final ReplyHandlerService replyHandler;

    // 위로 요청
    public void requestConsoleLetter(ReplyCreateCommand command, Long diaryId) {
        List<GptMessage> prompts = PromptProcessor.processLetterReplyPrompt(command);
        requestToGptApi(gptApiModel, prompts, diaryId, Reply.Type.LETTER);
    }

    // 공감 요청
    public void requestSympathyPhrase(ReplyCreateCommand command, Long diaryId) {
        List<GptMessage> prompts = PromptProcessor.processSympathyReplyPrompt(command);
        requestToGptApi(gptApiModel, prompts, diaryId, Reply.Type.SYMPATHY);
    }

    // 충고 요청
    public void requestAdvicePhrase(ReplyCreateCommand command, Long diaryId) {
        List<GptMessage> prompts = PromptProcessor.processAdviceReplyPrompt(command);
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
                .bodyValue(processGptRequestMessages(model, messages))
                .retrieve()
                .bodyToMono(GptResponseInfo.class)
                .doOnSubscribe(subscription -> {
                    putThreadId();
                    logHandler.logRequestMessages(messages);
                })
                .doOnError(error -> {
                    logHandler.logAndSaveErrorMessages(diaryId, error);
                })
                .map(this::processGptResponseMessages)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                        .jitter(0.5))
                .doFinally(signal -> MDC.clear())
                .subscribe(
                        gptResponse -> {
                            logHandler.logSuccessMessages(gptResponse);
                            replyHandler.createAndSaveReply(diaryId, gptResponse, type);
                        },
                        error -> {
                            replyHandler.treatFailedReplyDiary(diaryId);
                            gptCircuitBreaker.addFailureCount();
                        }
                );
    }

    // 스레드에 식별자값 부여
    private void putThreadId() {
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put("requestId", requestId);
    }

    // 요청 메세지 가공
    private Map<String, Object> processGptRequestMessages(String model, List<GptMessage> messages) {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("model", model);
        bodyMap.put("stream", false);
        bodyMap.put("messages", messages);
        bodyMap.put("temperature", 1.0);
        return bodyMap;
    }

    // 응답 메세지 가공
    private String processGptResponseMessages(GptResponseInfo gptResponse) {
        return gptResponse.getChoices().stream()
                .map(choice -> choice.getMessage().getContent())
                .toList()
                .get(0);
    }
}
