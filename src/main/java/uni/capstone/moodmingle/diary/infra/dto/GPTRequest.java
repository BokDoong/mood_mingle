package uni.capstone.moodmingle.diary.infra.dto;

import lombok.Data;

import java.util.List;

/**
 * 사용할 GPT Model 과 Prompt Message 를 담은 최종 GPT 요청 DTO 클래스
 *
 * @author ijin
 */
@Data
public class GPTRequest {
    /**
     * 모델, 프롬프트 메세지
     */
    private String model;
    private List<Message> messages;

    /**
     * 기본 생성자
     *
     * @param model GPT Model
     * @param messages Prompt Messages
     */
    public GPTRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages =  messages;
    }
}
