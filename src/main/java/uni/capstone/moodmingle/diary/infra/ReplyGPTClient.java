package uni.capstone.moodmingle.diary.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import uni.capstone.moodmingle.diary.application.LLMClient;
import uni.capstone.moodmingle.diary.application.ReplyCommandService;
import uni.capstone.moodmingle.diary.infra.dto.GptResponseInfo;
import uni.capstone.moodmingle.diary.infra.dto.GptMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static uni.capstone.moodmingle.diary.domain.Reply.*;

/**
 * 비동기 작업을 위해 WebClient 를 이용해서 GPT 와 통신하는 어댑터
 *
 * @author ijin
 */
@Component
@RequiredArgsConstructor
public class ReplyGPTClient implements LLMClient {

    private final ReplyCommandService replyCommandService;

    /**
     * API Key&Url&EndPoint, 각 기능별 Model
     */
    // Open Api
    @Value("${openai.api.url}")
    private String openAiRequestUrl;
    @Value("${openai.api.end-point}")
    private String openAiEndPoint;
    // Letter
    @Value("${openai.api.model.letter}")
    private String letterApiModel;
    @Value("${openai.api.key.letter}")
    private String letterApiKey;
    // Sympathy
    @Value("${openai.api.model.sympathy}")
    private String sympathyAPIModel;
    @Value("${openai.api.key.sympathy}")
    private String sympathyApiKey;
    // Advice
    @Value("${openai.api.model.advice}")
    private String adviceAPIModel;
    @Value("${openai.api.key.advice}")
    private String adviceApiKey;

    /**
     * 위로 편지 요청
     *
     * @param prompts Request Prompt Messages
     * @param diaryId 일기 ID
     */
    @Override
    public void requestLetter(List<GptMessage> prompts, Long diaryId) {
        requestToGptApi(letterApiModel, letterApiKey, prompts, diaryId, Type.LETTER);
    }

    /**
     * 공감 답변 요청
     *
     * @param prompts Request Prompt Messages
     * @param diaryId 일기 ID
     */
    @Override
    public void requestSympathyPhrase(List<GptMessage> prompts, Long diaryId) {
        requestToGptApi(sympathyAPIModel, sympathyApiKey, prompts, diaryId, Type.SYMPATHY);
    }

    /**
     * 충고 답변 요청
     *
     * @param prompts Request Prompt Messages
     * @param diaryId 일기 ID
     */
    @Override
    public void requestAdvicePhrase(List<GptMessage> prompts, Long diaryId) {
        requestToGptApi(adviceAPIModel, adviceApiKey, prompts, diaryId, Type.SYMPATHY);
    }

    /**
     * 실제로 GPT 와 통신하여 요청하는 메서드
     *
     * @param model    사용할 GPT Model
     * @param messages Prompt Messages
     * @param type 답장 Type
     * @return GPT 응답
     */
    private void requestToGptApi(String model, String apiKey, List<GptMessage> messages, Long diaryId, Type type) {

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
                        error -> respondGptCallBackFailedMessage(diaryId)
                );
    }

    private void respondGptCallBackSuccessMessage(String gptResponse, Long diaryId, Type type) {
        replyCommandService.createAndSaveReply(diaryId, gptResponse, type);
    }

    private void respondGptCallBackFailedMessage(Long diaryId) {
        replyCommandService.createAndSaveReply(diaryId, "네트워크 오류 발생..! 개발자에게 문의하세요.🥲🙇", null);
    }
}
