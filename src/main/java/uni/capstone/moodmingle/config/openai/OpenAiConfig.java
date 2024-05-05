package uni.capstone.moodmingle.config.openai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Spring 의 RestTemplate 를 이용하여 OpenAPI Key 를 등록하는 인터셉터를 추가하는 Config 클래스
 *
 * @author ijin
 */
@Configuration
public class OpenAiConfig {
    /**
     * OpenAI Key
     */
    @Value("${openai.api.key}")
    private String openAiKey;

    /**
     * RestTemplate 인터셉터
     *
     * @return 인터셉터 객체
     */
    @Bean
    public RestTemplate template(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + openAiKey);
            return execution.execute(request, body);
        });
        return restTemplate;
    }
}
