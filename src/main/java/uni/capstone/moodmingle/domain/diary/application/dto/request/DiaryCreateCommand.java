package uni.capstone.moodmingle.domain.diary.application.dto.request;

import org.springframework.web.multipart.MultipartFile;
import uni.capstone.moodmingle.domain.diary.domain.Diary;

import java.time.LocalDate;

/**
 * Presentation 계층으로부터 받는 일기 생성 DTO
 *
 * @author ijin
 */
public record DiaryCreateCommand(Long memberId, String title, String content,
                                 LocalDate date, Diary.Emotion emotion, Diary.Weather weather, MultipartFile image) {
}
