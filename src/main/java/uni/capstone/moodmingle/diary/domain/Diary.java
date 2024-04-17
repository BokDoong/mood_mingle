package uni.capstone.moodmingle.diary.domain;

import jakarta.persistence.*;
import lombok.*;
import uni.capstone.moodmingle.member.domain.Member;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "title")
    private String title;
    @Column(name = "date")
    private LocalDateTime date;
    @Column(name = "content")
    private String content;
    @Column(name = "image_url")
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    @Column(name = "emotion")
    private Emotion emotion;
    @Enumerated(EnumType.STRING)
    @Column(name = "weather")
    private Weather weather;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private Reply reply;

    @Builder
    public Diary(String title, LocalDateTime date, String content, String imageUrl, Emotion emotion, Weather weather) {
        this.title = title;
        this.date = date;
        this.content = content;
        this.imageUrl = imageUrl.isEmpty() ? "" : imageUrl;
        this.emotion = emotion;
        this.weather = weather;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Emotion {
        JOY("기쁨"),
        FEAR("공포"),
        CALMNESS("평온"),
        ANGER("분노"),
        LOVE("사랑"),
        DEPRESSED("슬픔"),
        IMPRESSED("감동"),
        WORRIED("걱정"),
        CONFIDENT("자신감"),
        LETHARGY("무기력"),
        ;

        private final String value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Weather {
        // 화창한, 흐린, 비오는, 눈오는
        SUNNY("화창함"),
        CLOUDY("흐린"),
        RAINY("비오는"),
        SNOWY("눈오는"),
        ;

        private final String value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum SnsType {
        KAKAO, NAVER,
    }
}
