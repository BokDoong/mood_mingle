package uni.capstone.moodmingle.diary.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import uni.capstone.moodmingle.common.validator.ValidEnum;

import java.time.LocalDate;

import static uni.capstone.moodmingle.diary.domain.Diary.*;

/**
 * Client 로부터 받을 일기 생성 DTO
 *
 * @author ijin
 */
@Getter
public class DiaryCreateDto {
    @NotBlank(message = "title is blank")
    private String title;
    @NotNull(message = "date is null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd", timezone = "Asia/Seoul")
    private LocalDate date;
    @NotBlank(message = "content is blank")
    private String content;
    @ValidEnum(enumClass = Emotion.class, message = "Emotion Enum Data is invalid")
    private Emotion emotion;
    @ValidEnum(enumClass = Weather.class, message = "Weather Enum Data is invalid")
    private Weather weather;
}
