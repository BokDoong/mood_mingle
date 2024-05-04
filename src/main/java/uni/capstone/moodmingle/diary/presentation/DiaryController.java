package uni.capstone.moodmingle.diary.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uni.capstone.moodmingle.diary.application.DiaryCommandService;
import uni.capstone.moodmingle.diary.application.dto.request.DiaryCreateCommand;
import uni.capstone.moodmingle.diary.presentation.dto.DiaryDtoMapper;
import uni.capstone.moodmingle.diary.presentation.dto.request.DiaryCreateDto;

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

    /**
     * 일기 생성(1) - 위로편지 답장
     */
    @PostMapping("/api/v1/diary/letter")
    public void replyLetter(@RequestPart("dto") @Valid DiaryCreateDto dto, @RequestPart(value = "image", required = false) MultipartFile image) {
        diaryCommandService.replyDiaryWithLetter(toCreateCommand(dto, image));
    }

    @PostMapping("/test")
    public DiaryCreateDto replyLetter(@RequestBody @Valid DiaryCreateDto dto) {
        return dto;
    }

    /**
     * 일기 생성(2) - 공감 답장
     */
    @PostMapping("/api/v1/diary/sympathy")
    public void replySympathy(@RequestPart @Valid DiaryCreateDto dto, @RequestPart(required = false) MultipartFile image) {

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
