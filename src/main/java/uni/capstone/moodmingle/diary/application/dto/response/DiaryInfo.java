package uni.capstone.moodmingle.diary.application.dto.response;

import java.time.LocalDate;

import static uni.capstone.moodmingle.diary.domain.Diary.*;

public record DiaryInfo(Long diaryId, LocalDate date, Emotion emotion) {
}
