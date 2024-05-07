package uni.capstone.moodmingle.diary.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * 일기 상세 조회를 위한 DTO
 *
 * @author ijin
 */
@Getter
public class DairyDetailQueryDto {
    @NotNull
    private Long memberId;
    @NotNull
    private Long diaryId;
}
