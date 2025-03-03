package uni.capstone.moodmingle.clients.llm.gpt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GptWebClientConfig {

    @Value("${openai.api.end-point}")
    private String gptEndPoint;
    @Value("${openai.api.key}")
    private String gptApiKey;

    @Bean
    public WebClient gptWebClient() {
        return WebClient.builder()
                .baseUrl(gptEndPoint)
                .defaultHeader("Authorization", "Bearer " + gptApiKey)
                .defaultHeader("Content-Type", "application/json;charset=utf-8")
                .build();
    }
}
