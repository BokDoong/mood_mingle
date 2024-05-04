package uni.capstone.moodmingle.diary.infra.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * GPT 로부터 받는 답변을 담을 DTO 클래스
 *
 * @author ijin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GPTResponse {
    /**
     * 내부 클래스 Choice 를 담은 필드
     */
    private List<Choice> choices;

    /**
     * 내부 클래스, GPT 는 프롬프트에 대한 답변을 여기다가 넣어서 준다.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Choice {
        private int index;
        private Message message;
    }
}
