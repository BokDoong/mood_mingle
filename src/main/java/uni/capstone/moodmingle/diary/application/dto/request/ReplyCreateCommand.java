package uni.capstone.moodmingle.diary.application.dto.request;

import uni.capstone.moodmingle.diary.domain.Diary;

import java.time.LocalDate;

/**
 * Diary 와 Member 의 정보를 바탕으로 LLM 에 답장을 받고, DB에 저장하기 위한 DTO
 *
 * @author ijin
 */
public record ReplyCreateCommand(String memberName, String title, String content,
                                 LocalDate date, Diary.Emotion emotion) {
}
