package uni.capstone.moodmingle.diary.infra.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * GPT 로부터 받는 답변을 담을 DTO 클래스
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
    private LinkedHashMap<String, Integer> usage;

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
}
