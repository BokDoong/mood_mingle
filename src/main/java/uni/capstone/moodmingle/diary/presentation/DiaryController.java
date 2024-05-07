package uni.capstone.moodmingle.diary.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uni.capstone.moodmingle.diary.application.DiaryCommandService;
import uni.capstone.moodmingle.diary.application.DiaryQueryService;
import uni.capstone.moodmingle.diary.application.dto.request.DiaryCreateCommand;
import uni.capstone.moodmingle.diary.application.dto.response.DiaryInfo;
import uni.capstone.moodmingle.diary.presentation.dto.DiaryDtoMapper;
import uni.capstone.moodmingle.diary.presentation.dto.request.DiaryCreateDto;
import uni.capstone.moodmingle.diary.presentation.dto.request.MonthlyDiariesQueryDto;

import java.util.List;

/**
 * 일기 도메인 컨트롤러
 *
 * @author ijin
 */
@RestController
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryDtoMapper mapper;
    private final DiaryCommandService diaryCommandService;
    private final DiaryQueryService diaryQueryService;

    /**
     * 일기 생성(1) - 위로편지 답장
     */
    @PostMapping("/api/v1/diary/letter")
    public void replyLetter(@RequestPart("dto") @Valid DiaryCreateDto dto,
                            @RequestPart(value = "image", required = false) MultipartFile image) {
        diaryCommandService.replyDiaryWithLetter(toCreateCommand(dto, image));
    }

    /**
     * 일기 생성(2) - 공감 답장
     */
    @PostMapping("/api/v1/diary/sympathy")
    public void replySympathy(@RequestPart("dto") @Valid DiaryCreateDto dto,
                              @RequestPart(value = "image", required = false) MultipartFile image) {
        diaryCommandService.replyDiaryWithSympathy(toCreateCommand(dto, image));
    }

    /**
     * 일기 생성(3) - 충고 답장
     */
    @PostMapping("/api/v1/diary/advice")
    public void replyAdvice(@RequestPart @Valid DiaryCreateDto dto, @RequestPart(required = false) MultipartFile image) {

    }

    /**
     * 월별 일기 조회
     */
    @GetMapping("/api/v1/diary")
    public List<DiaryInfo> getMonthlyDiaryInfos(@RequestBody @Valid MonthlyDiariesQueryDto dto) {
        return diaryQueryService.findMonthlyDiaryInfos(dto.getMemberId(), dto.toLocalDate());
    }

    /**
     * 일기 상세조회
     */


    /**
     * 일기 수정
     */


    /**
     * 일기 삭제
     */


    private DiaryCreateCommand toCreateCommand(DiaryCreateDto dto, MultipartFile image) {
        return mapper.toCommand(dto, image);
    }
}
