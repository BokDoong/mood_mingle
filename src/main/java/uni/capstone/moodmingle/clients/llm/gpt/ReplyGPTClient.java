package uni.capstone.moodmingle.clients.llm.gpt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import uni.capstone.moodmingle.clients.llm.LLMClient;
import uni.capstone.moodmingle.clients.llm.gpt.dto.GptMessage;
import uni.capstone.moodmingle.clients.llm.gpt.dto.GptResponseInfo;
import uni.capstone.moodmingle.clients.llm.gpt.facade.PromptProcessingFacade;
import uni.capstone.moodmingle.domain.diary.application.ReplyCommandService;
import uni.capstone.moodmingle.domain.diary.application.dto.request.ReplyCreateCommand;
import uni.capstone.moodmingle.domain.diary.domain.Reply;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 비동기 작업을 위해 WebClient 를 이용해서 GPT 와 통신하는 어댑터
 *
 * @author ijin
 */
@Component
@RequiredArgsConstructor
public class ReplyGPTClient implements LLMClient {

    private final ReplyCommandService replyCommandService;
    private final PromptProcessingFacade processingFacade;

    // OpenAI API 설정값
    @Value("${openai.api.url}")
    private String openAiRequestUrl;
    @Value("${openai.api.end-point}")
    private String openAiEndPoint;
    @Value("${openai.api.model}")
    private String openAiApiModel;
    @Value("${openai.api.key}")
    private String openAiApiKey;

    // 위로 요청
    @Override
    public void requestConsoleLetter(ReplyCreateCommand command, Long diaryId) {
        // LLM Request Message 가공
        List<GptMessage> prompts = processingFacade.processLetterReplyPrompt(command);
        requestToGptApi(openAiApiModel, openAiApiKey, prompts, diaryId, Reply.Type.LETTER);
    }

    // 공감 요청
    @Override
    public void requestSympathyPhrase(ReplyCreateCommand command, Long diaryId) {
        // LLM Request Message 가공
        List<GptMessage> prompts = processingFacade.processSympathyReplyPrompt(command);
        requestToGptApi(openAiApiModel, openAiApiKey, prompts, diaryId, Reply.Type.SYMPATHY);
    }

    // 충고 요청
    @Override
    public void requestAdvicePhrase(ReplyCreateCommand command, Long diaryId) {
        List<GptMessage> prompts = processingFacade.processAdviceReplyPrompt(command);
        requestToGptApi(openAiApiModel, openAiApiKey, prompts, diaryId, Reply.Type.ADVICE);
    }

    /**
     * 실제로 GPT 와 통신하여 요청하는 메서드
     *
     * @param model    사용할 GPT Model
     * @param messages Prompt Messages
     * @param type 답장 Type
     * @return GPT 응답
     */
    private void requestToGptApi(String model, String apiKey, List<GptMessage> messages,
                                 Long diaryId, Reply.Type type) {

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("model", model);
        bodyMap.put("stream", false);
        bodyMap.put("messages", messages);
        bodyMap.put("temperature", 1.0);

        WebClient webClient =
                WebClient
                        .builder()
                        .baseUrl(openAiEndPoint)
                        .build();

        webClient
                .post()
                .uri(openAiRequestUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json;charset=utf-8")
                .bodyValue(bodyMap)
                .retrieve()
                .bodyToMono(GptResponseInfo.class)
                .flatMap(gptResponse -> {
                    List<String> contents = gptResponse.getChoices().stream()
                            .map(choice -> choice.getMessage().getContent())
                            .collect(Collectors.toList());
                    return Mono.just(contents.get(0));     // GptResponseInfo -> Content 인 응답 내용만 Mono Type 으로 뽑아온다.
                })
                .retry(3)       // 실패해도 3번 시도
                .subscribe(
                        gptResponse -> respondGptCallBackSuccessMessage(gptResponse, diaryId, type),
                        error -> respondGptCallBackFailedMessage(error, diaryId)
                );
    }

    private void respondGptCallBackSuccessMessage(String gptResponse, Long diaryId, Reply.Type type) {
        replyCommandService.createAndSaveReply(diaryId, gptResponse, type);
    }

    private void respondGptCallBackFailedMessage(Throwable error, Long diaryId) {
        System.out.println(error.getMessage());
        replyCommandService.createAndSaveReply(diaryId, "네트워크 오류 발생..! 개발자에게 문의하세요.🥲🙇", null);
    }
}
