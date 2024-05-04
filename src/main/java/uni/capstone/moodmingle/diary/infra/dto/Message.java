package uni.capstone.moodmingle.diary.infra.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * GPT 와 주고 받을 메세지 DTO
 *
 * @author ijin
 */
@Data
@NoArgsConstructor
public class Message implements Serializable {
    /**
     * GPT Role, Request&Response Content
     */
    private String role;
    private String content;

    /**
     * 기본 생성자.
     *
     * @param role GPT Role
     * @param content GPT Request&Response Content
     */
    @Builder
    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }
}
