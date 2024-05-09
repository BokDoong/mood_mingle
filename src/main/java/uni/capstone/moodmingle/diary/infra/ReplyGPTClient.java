package uni.capstone.moodmingle.diary.infra;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import uni.capstone.moodmingle.diary.application.LLMClient;
import uni.capstone.moodmingle.diary.infra.dto.GptResponseInfo;
import uni.capstone.moodmingle.diary.infra.dto.GptMessage;
import uni.capstone.moodmingle.exception.BusinessException;
import uni.capstone.moodmingle.exception.code.ErrorCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 비동기 작업을 위해 WebClient 를 이용해서 GPT 와 통신하는 어댑터
 *
 * @author ijin
 */
@Slf4j
@Component
public class ReplyGPTClient implements LLMClient {

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
     * @return GPT 에서 받은 위로 편지
     */
    @Override
    public String requestLetter(List<GptMessage> prompts) {
        return requestToGptApi(letterAPIModel, prompts);
    }

    /**
     * 공감 답변 요청
     *
     * @param prompts Request Prompt Messages
     * @return GPT 에서 받은 공감 답변
     */
    @Override
    public String requestSympathyPhrase(List<GptMessage> prompts) {
        return requestToGptApi(sympathyAPIModel, prompts);
    }

    /**
     * 실제로 GPT 와 통신하여 요청하는 메서드
     *
     * @param model 사용할 GPT Model
     * @param messages Prompt Messages
     * @return GPT 응답
     */
    private String requestToGptApi(String model, List<GptMessage> messages) {

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("model", model);
        bodyMap.put("stream", false);
        bodyMap.put("messages", messages);

        WebClient webClient =
                WebClient
                        .builder()
                        .baseUrl(openAiEndPoint)
                        .build();

        GptResponseInfo responseInfo = webClient
                .post()
                .uri(openAiRequestUrl)
                .header("Authorization", "Bearer " + openAiKey)
                .header("Content-Type", "application/json;charset=utf-8")
                .bodyValue(bodyMap)
                .retrieve()
                .bodyToMono(GptResponseInfo.class)
                .retry(3)   // 실패해도 3번은 시도할 수 있게 설정
                .block();

        return parseGptResponseMessage(responseInfo);
    }

    private String parseGptResponseMessage(GptResponseInfo responseInfo) {
        return responseInfo.getChoices().get(0).getMessage().getContent();
    }
}
