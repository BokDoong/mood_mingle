package uni.capstone.moodmingle.clients.llm.gpt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * GPT 로부터의 응답 정보를 담을 DTO 클래스
 *
 * @author ijin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GptResponseInfo {

    private String id;
    private String object;
    private Integer created;
    private String model;
    private List<Choice> choices;
    private GptUsage usage;

    /**
     * GPT 의 답변 메세지를 담는 내부 클래스
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Choice {
        private int index;
        private GptMessage message;
        private Object logprobs;
        private String finish_reason;
    }

    /**
     * OpenAI API의 `usage` 필드를 위한 클래스
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GptUsage {
        private Integer prompt_tokens;
        private Integer completion_tokens;
        private Integer total_tokens;
    }
}
