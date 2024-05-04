package uni.capstone.moodmingle.diary.application.dto.request;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

import static uni.capstone.moodmingle.diary.domain.Diary.*;

/**
 * Presentation 계층으로부터 받는 일기 생성 DTO
 *
 * @author ijin
 */
public record DiaryCreateCommand(Long memberId, String title, String content,
                                 LocalDate date, Emotion emotion, Weather weather, MultipartFile image) {
}
