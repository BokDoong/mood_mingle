package uni.capstone.moodmingle.domain.diary.application.dto.response;

import uni.capstone.moodmingle.domain.diary.domain.Diary;

import java.time.LocalDate;

/**
 * 월별 일기 조회시, 응답 레코드
 *
 * @param diaryId
 * @param date
 * @param emotion
 */
public record DiaryInfo(Long diaryId, LocalDate date, Diary.Emotion emotion) {
}
