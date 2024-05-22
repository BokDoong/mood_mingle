package uni.capstone.moodmingle.diary.application.dto.response;

import lombok.Getter;
import uni.capstone.moodmingle.diary.domain.DiaryReplyCrypto;

import java.time.LocalDate;

import static uni.capstone.moodmingle.diary.domain.Diary.*;
import static uni.capstone.moodmingle.diary.domain.Reply.*;

/**
 * 일기 상세 조회시, 응답 레코드
 */
@Getter
public class DiaryDetailInfo {
    private Long diaryId;
    private String title;
    private String content;
    private LocalDate date;
    private Emotion emotion;
    private Weather weather;
    private String imageUrl;
    private String replyContent;
    private Type type;

    public DiaryDetailInfo(Long diaryId, String title, String content, LocalDate date, Emotion emotion,
                           Weather weather, String imageUrl, String replyContent, Type type) throws Exception {
        this.diaryId = diaryId;
        this.title = title;
        this.content = DiaryReplyCrypto.decrypt(content);
        this.date = date;
        this.emotion = emotion;
        this.weather = weather;
        this.imageUrl = imageUrl;
        this.replyContent = DiaryReplyCrypto.decrypt(replyContent);
        this.type = type;
    }
}
