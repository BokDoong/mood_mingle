package uni.capstone.moodmingle.diary.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

import static uni.capstone.moodmingle.diary.domain.Diary.*;
import static uni.capstone.moodmingle.diary.domain.Reply.*;

/**
 * 일기 상세 조회시, 응답 레코드
 */
@Getter
@AllArgsConstructor
public class DiaryDetailInfo {
    private Long diaryId;
    private String title;
    private String content;
    private LocalDate date;
    private Emotion emotion;
    private Weather weather;
    @Nullable
    private String imageUrl;
    @Nullable
    private String replyContent;
    @Nullable
    private Type type;
}
