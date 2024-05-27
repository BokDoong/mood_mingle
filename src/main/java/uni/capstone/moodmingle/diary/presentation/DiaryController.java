package uni.capstone.moodmingle.diary.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uni.capstone.moodmingle.config.security.jwt.entity.JwtUserDetails;
import uni.capstone.moodmingle.diary.application.DiaryCommandService;
import uni.capstone.moodmingle.diary.application.DiaryQueryService;
import uni.capstone.moodmingle.diary.application.dto.request.DiaryCreateCommand;
import uni.capstone.moodmingle.diary.application.dto.response.DiaryDetailInfo;
import uni.capstone.moodmingle.diary.application.dto.response.DiaryInfo;
import uni.capstone.moodmingle.diary.presentation.dto.DiaryDtoMapper;
import uni.capstone.moodmingle.diary.presentation.dto.request.DiaryCreateDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import static uni.capstone.moodmingle.diary.domain.Reply.*;

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
    public void replyLetter(@AuthenticationPrincipal JwtUserDetails userDetails, @RequestPart("dto") @Valid DiaryCreateDto dto,
                            @RequestPart(value = "image", required = false) MultipartFile image) {
        Long memberId = userDetails.getUserId();
        diaryCommandService.createAndSaveDiary(toCreateCommand(memberId, dto, image), Type.LETTER);
    }

    /**
     * 일기 생성(2) - 공감 답장
     */
    @PostMapping("/api/v1/diary/sympathy")
    public void replySympathy(@AuthenticationPrincipal JwtUserDetails userDetails, @RequestPart("dto") @Valid DiaryCreateDto dto,
                              @RequestPart(value = "image", required = false) MultipartFile image) {
        Long memberId = userDetails.getUserId();
        diaryCommandService.createAndSaveDiary(toCreateCommand(memberId, dto, image), Type.SYMPATHY);
    }

    /**
     * 일기 생성(3) - 충고 답장
     */
    @PostMapping("/api/v1/diary/advice")
    public void replyAdvice(@AuthenticationPrincipal JwtUserDetails userDetails, @RequestPart("dto") @Valid DiaryCreateDto dto,
                            @RequestPart(value = "image", required = false) MultipartFile image) {
        Long memberId = userDetails.getUserId();
        diaryCommandService.createAndSaveDiary(toCreateCommand(memberId, dto, image), Type.ADVICE);
    }

    /**
     * 월별 일기 조회
     */
    @GetMapping("/api/v1/diary")
    public List<DiaryInfo> getMonthlyDiaryInfos(@AuthenticationPrincipal JwtUserDetails userDetails,
                                                @RequestParam("date") @DateTimeFormat(pattern = "yyyy/MM") String date) {
        Long memberId = userDetails.getUserId();
        return diaryQueryService.findMonthlyDiaryInfos(memberId, toLocalDate(date));
    }

    /**
     * 일기 상세조회
     */
    @GetMapping("/api/v1/diary/detail")
    public DiaryDetailInfo getDiaryDetailInfo(@AuthenticationPrincipal JwtUserDetails userDetails,
                                              @RequestParam("diaryId") Long diaryId) {
        Long memberId = userDetails.getUserId();
        return diaryQueryService.findDiaryDetailInfo(memberId, diaryId);
    }

    /**
     * 월별 감정통계 조회
     */
    @GetMapping("/api/v1/diary/monthly-emotion")
    public HashMap<String, Integer> getMonthlyEmotionsInfo(@AuthenticationPrincipal JwtUserDetails userDetails,
                                                           @RequestParam("date") @DateTimeFormat(pattern = "yyyy/MM") String date) {
        Long memberId = userDetails.getUserId();
        return diaryQueryService.MonthlyEmotionsInfo(memberId, toLocalDate(date));
    }

    /**
     * "yyyy/MM" 형식의 문자열을 LocalDate 로 변환
     *
     * @return 변환된 LocalDate
     */
    private LocalDate toLocalDate(String date) {
        return LocalDate.parse(date + "/01", DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    private DiaryCreateCommand toCreateCommand(Long memberId, DiaryCreateDto dto, MultipartFile image) {
        return mapper.toCommand(memberId, dto, image);
    }
}
