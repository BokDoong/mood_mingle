package uni.capstone.moodmingle.diary.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uni.capstone.moodmingle.diary.application.LLMClient;
import uni.capstone.moodmingle.diary.infra.dto.GPTRequest;
import uni.capstone.moodmingle.diary.infra.dto.GPTResponse;
import uni.capstone.moodmingle.diary.infra.dto.Message;
import uni.capstone.moodmingle.exception.BusinessException;
import uni.capstone.moodmingle.exception.code.ErrorCode;

import java.util.List;

/**
 * GPT 와 통신하여 답변을 받아오는 클래스
 *
 * @author ijin
 */
@Component
@RequiredArgsConstructor
public class ReplyGPTClient implements LLMClient {

    /**
     * API Key, OpenAI URL
     */
    @Value("${openai.api.letter-model}")
    private String letterAPIKey;
    @Value("${openai.api.sympathy-model}")
    private String sympathyAPIKey;
    @Value("${openai.api.url}")
    private String apiURL;

    /**
     * OpenAI 와 주고받게 되는 RestTemplateModel
     */
    private final RestTemplate restTemplate;

    /**
     * GPT 에게 위로 편지 답변 요청
     *
     * @param messages 프롬프트 메세지
     * @return GPT 응답 메세지
     */
    @Override
    public String requestLetter(List<Message> messages) {
        GPTRequest request = new GPTRequest(letterAPIKey, messages);
        return requestToGptApi(request);
    }

    /**
     * GPT 에게 공감 답변 요청
     *
     * @param messages 프롬프트 메세지
     * @return GPT 응답 메세지
     */
    @Override
    public String requestSympathyPhrase(List<Message> messages) {
        GPTRequest request = new GPTRequest(sympathyAPIKey, messages);
        return requestToGptApi(request);
    }

    private String requestToGptApi(GPTRequest request) {
        try {
            GPTResponse response = restTemplate.postForObject(apiURL, request, GPTResponse.class);
            return response.getChoices().get(0).getMessage().getContent();
        } catch (Error error) {
            throw new BusinessException(ErrorCode.FAILED_LLM_NETWORKING);
        }
    }
}

