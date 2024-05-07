package uni.capstone.moodmingle.diary.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Client 로부터 받을 월별 일기 조회 DTO
 *
 * @author ijin
 */
@Getter
public class MonthlyDiariesQueryDto {
    @NotNull
    private Long memberId;
    @NotNull(message = "date is null")
    @DateTimeFormat(pattern = "yyyy/MM")
    private String date;

    /**
     * "yyyy/MM" 형식의 문자열을 LocalDate 로 변환
     *
     * @return 변환된 LocalDate
     */
    public LocalDate toLocalDate() {
        return LocalDate.parse(date + "/01", DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }
}
