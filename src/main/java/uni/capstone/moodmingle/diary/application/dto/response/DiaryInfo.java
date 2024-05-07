package uni.capstone.moodmingle.diary.application.dto.response;

import java.time.LocalDate;

import static uni.capstone.moodmingle.diary.domain.Diary.*;

/**
 * 월별 일기 조회시, 응답 레코드
 *
 * @param diaryId
 * @param date
 * @param emotion
 */
public record DiaryInfo(Long diaryId, LocalDate date, Emotion emotion) {
}
