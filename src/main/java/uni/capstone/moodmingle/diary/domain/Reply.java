package uni.capstone.moodmingle.diary.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "content")
    private String content;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @Builder
    public Reply(String content, Type type) {
        this.content = content;
        this.type = type;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Type {
        LETTER("편지"),
        FRIENDLINESS("친근함"),
        ADVICE("충고"),
        ;

        private final String value;
    }
}
