package uni.capstone.moodmingle.diary.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Reply 엔티티
 *
 * @author ijin
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Reply {

    /**
     * 내용, 답변 타입
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "content", length = 5000)
    private String content;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    /**
     * 기본 생성자
     *
     * @param content LLM 으로부터 받은 답장 내용
     * @param type Client 가 선택한 답장 타입
     */
    @Builder
    public Reply(String content, Type type) {
        this.content = DiaryReplyCrypto.encrypt(content);
        this.type = type;
    }

    /**
     * 답변 타입 Enum
     */
    @Getter
    @RequiredArgsConstructor
    public enum Type {
        LETTER("편지"),
        SYMPATHY("친근함"),
        ADVICE("충고"),
        ;

        private final String value;
    }
}
