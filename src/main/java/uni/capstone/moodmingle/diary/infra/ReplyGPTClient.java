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
    @Value("${openai.api.key}")
    private String openAiKey;
    @Value("${openai.api.url}")
    private String openAiRequestUrl;
    @Value("${openai.api.end-point}")
    private String openAiEndPoint;
    @Value("${openai.api.letter-model}")
    private String letterAPIModel;
    @Value("${openai.api.sympathy-model}")
    private String sympathyAPIModel;

    /**
     * 위로 편지 요청
     *
     * @param prompts Request Prompt Messages
     * @param diaryId 일기 ID
     * @return GPT 에서 받은 위로 편지
     */
    @Override
    public void requestLetter(List<GptMessage> prompts, Long diaryId) {
        requestToGptApi(letterAPIModel, prompts, diaryId, Type.LETTER);
    }

    /**
     * 공감 답변 요청
     *
     * @param prompts Request Prompt Messages
     * @param diaryId 일기 ID
     * @return GPT 에서 받은 공감 답변
     */
    @Override
    public void requestSympathyPhrase(List<GptMessage> prompts, Long diaryId) {
        requestToGptApi(sympathyAPIModel, prompts, diaryId, Type.SYMPATHY);
    }

    /**
     * 실제로 GPT 와 통신하여 요청하는 메서드
     *
     * @param model    사용할 GPT Model
     * @param messages Prompt Messages
     * @param type 답장 Type
     * @return GPT 응답
     */
    private void requestToGptApi(String model, List<GptMessage> messages, Long diaryId, Type type) {

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("model", model);
        bodyMap.put("stream", false);
        bodyMap.put("messages", messages);

        WebClient webClient =
                WebClient
                        .builder()
                        .baseUrl(openAiEndPoint)
                        .build();

        webClient
                .post()
                .uri(openAiRequestUrl)
                .header("Authorization", "Bearer " + openAiKey)
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
