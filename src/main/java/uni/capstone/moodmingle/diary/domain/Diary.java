package uni.capstone.moodmingle.diary.domain;

import jakarta.persistence.*;
import lombok.*;
import uni.capstone.moodmingle.member.domain.Member;

import java.time.LocalDate;

/**
 * Diary 엔티티
 *
 * @author ijin
 */
@Entity
@Table(name = "tb_diary")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Diary {

    /**
     * 제목, 날짜, 내용, 이미지 값객체, 감정, 날씨
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "title")
    private String title;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "content", length = 5000)
    private String content;
    @Embedded
    private Image image;
    @Enumerated(EnumType.STRING)
    @Column(name = "emotion")
    private Emotion emotion;
    @Enumerated(EnumType.STRING)
    @Column(name = "weather")
    private Weather weather;

    /**
     * JPA 연관관계 엔티티
     * 1.사용자(member) - 다대일
     * 2.답장(reply) - 일대일
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "reply_id")
    private Reply reply;

    /**
     * 기본 생성자.
     * 답장은 LLM 으로부터 생성된 후, 넣기위하여 제거.
     */
    @Builder
    public Diary(String title, LocalDate date, String content,
                 Emotion emotion, Weather weather, Member member) {
        this.member = member;
        this.title = title;
        this.date = date;
        this.content = DiaryReplyCrypto.encrypt(content);
        this.emotion = emotion;
        this.weather = weather;
        this.image = new Image();
    }

    /**
     * Reply 객체 연관관계 추가
     *
     * @param reply LLM 으로부터 받은 답장
     */
    public void putReply(Reply reply) {
        this.reply = reply;
    }

    /**
     * 이미지 URL 추가 -> Image 객체 생성
     *
     * @param imageUrl File DB 에 저장하고 받은 이미지 URL
     */
    public void putImage(String imageUrl) {
        this.image = Image.builder()
                .imageUrl(imageUrl)
                .build();
    }

    /**
     * 감정 상태 Enum
     */
    @Getter
    @RequiredArgsConstructor
    public enum Emotion {
        PLEASURE("기쁨"),
        FEAR("공포"),
        PEACE("평온"),
        ANGER("분노"),
        FLUTTER("사랑"),
        SADNESS("슬픔"),
        MOVED("감동"),
        WORRY("걱정"),
        CONFIDENCE("자신감"),
        LETHARGY("무기력"),
        ;

        private final String value;
    }

    /**
     * 날씨 정보 Enum
     */
    @Getter
    @RequiredArgsConstructor
    public enum Weather {
        SUNNY("화창함"),
        CLOUDY("흐린"),
        RAINY("비오는"),
        ;

        private final String value;
    }
}
