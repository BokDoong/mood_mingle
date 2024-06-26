package uni.capstone.moodmingle.diary.application.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static uni.capstone.moodmingle.diary.domain.Diary.*;
import static uni.capstone.moodmingle.diary.domain.Reply.*;

/**
 * 일기 상세 조회시, 응답 레코드
 */
@Getter
@NoArgsConstructor
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
                           Weather weather, String imageUrl, String replyContent, Type type) {
        this.diaryId = diaryId;
        this.title = title;
        this.content = content;
        this.date = date;
        this.emotion = emotion;
        this.weather = weather;
        this.imageUrl = imageUrl;
        this.replyContent = (replyContent == null || replyContent.isEmpty()) ? null : replyContent;
        this.type = type == null ? null : type;
    }

    public void setDecryptedDiaryContent(String decryptedDiaryContent) {
        this.content = decryptedDiaryContent;
    }

    public void setDecryptedReplyContent(String decryptedReplyContent) {
        this.replyContent = decryptedReplyContent;
    }

    public boolean verifyReplyContentEmpty() {
        return replyContent == null || replyContent.isEmpty();
    }
}
