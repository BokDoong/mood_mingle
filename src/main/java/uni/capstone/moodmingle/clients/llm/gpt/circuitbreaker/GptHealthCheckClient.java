package uni.capstone.moodmingle.clients.llm.gpt.circuitbreaker;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import uni.capstone.moodmingle.clients.llm.gpt.dto.GptMessage;
import uni.capstone.moodmingle.clients.llm.gpt.dto.GptResponseInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GptHealthCheckClient {

    // OpenAI API 설정값
    @Value("${openai.api.url}")
    private String gptRequestUrl;
    @Value("${openai.api.model}")
    private String gptApiModel;

    private final WebClient gptWebClient;

    // GPT API 테스트
    public Boolean requestToGptApi() {
        Map<String, Object> bodyMap = processOpenAIRequestBody();
        try {
            gptWebClient
                    .post()
                    .uri(gptRequestUrl)
                    .bodyValue(bodyMap)
                    .retrieve()
                    .bodyToMono(GptResponseInfo.class)
                    .block();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // OpenAI API 요청 데이터 가공
    private Map<String, Object> processOpenAIRequestBody() {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("model", gptApiModel);
        bodyMap.put("stream", false);
        bodyMap.put("messages", processTestMessages());
        bodyMap.put("temperature", 1.0);
        return bodyMap;
    }

    private List<GptMessage> processTestMessages() {
        List<GptMessage> messages = new ArrayList<>();
        GptMessage message = GptMessage.builder()
                .role("user")
                .content("Hello!")
                .build();
        messages.add(message);
        return messages;
    }
}
