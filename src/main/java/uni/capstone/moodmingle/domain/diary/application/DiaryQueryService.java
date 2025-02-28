package uni.capstone.moodmingle.domain.diary.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uni.capstone.moodmingle.domain.diary.application.dto.response.DiaryDetailInfo;
import uni.capstone.moodmingle.domain.diary.application.dto.response.DiaryInfo;
import uni.capstone.moodmingle.domain.diary.domain.Diary;
import uni.capstone.moodmingle.domain.diary.domain.DiaryCrypto;
import uni.capstone.moodmingle.domain.diary.domain.DiaryRepository;
import uni.capstone.moodmingle.domain.diary.domain.EmotionCalculator;
import uni.capstone.moodmingle.domain.diary.exception.DiaryNotFoundException;
import uni.capstone.moodmingle.domain.member.application.MemberQueryService;
import uni.capstone.moodmingle.domain.member.domain.Member;
import uni.capstone.moodmingle.global.error.ErrorCode;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

/**
 * Diary 도메인 조회 로직 담당하는 애플리케이션
 *
 * @author ijin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryQueryService {

    private final MemberQueryService memberQueryService;
    private final DiaryRepository diaryRepository;

    private final DiaryCrypto diaryCrypto;
    private final EmotionCalculator emotionCalculator;

    // 월별 일기 조회
    @Transactional(readOnly = true)
    public List<DiaryInfo> findMonthlyDiaryInfos(Long memberId, LocalDate date) {
        return diaryRepository.findMonthlyDiaryInfos(memberId, date);
    }

    // 일기 상세 조회
    @Transactional(readOnly = true)
    public DiaryDetailInfo findDiaryDetailInfo(Long memberId, Long diaryId) {
        Member member = memberQueryService.findMember(memberId);
        DiaryDetailInfo diaryDetailInfo = getDiaryDetailInfo(memberId, diaryId);
        return setDecryptedInfos(diaryDetailInfo, member);
    }

    // 월별 감정 통계 조회
    public HashMap<String, Integer> MonthlyEmotionsInfo(Long memberId, LocalDate date) {
        List<Diary.Emotion> monthlyEmotions = diaryRepository.findMonthlyEmotionsInfo(memberId, date);
        return emotionCalculator.makeStatisticsOfEmotions(monthlyEmotions);
    }

    // DTO 에 복호화한 일기 내용 저장
    private DiaryDetailInfo setDecryptedInfos(DiaryDetailInfo diaryDetailInfo, Member member) {
        // 개인키
        String privateKey = member.getSecretKey();
        // 일기 복호화
        diaryDetailInfo.setDecryptedDiaryContent(getDecryptedContent(privateKey, diaryDetailInfo.getContent()));
        // 답장 복호화
        if (!diaryDetailInfo.verifyReplyContentEmpty()) {
            diaryDetailInfo.setDecryptedReplyContent(getDecryptedContent(privateKey, diaryDetailInfo.getReplyContent()));
        }
        return diaryDetailInfo;
    }

    private String getDecryptedContent(String privateKey, String content) {
        return diaryCrypto.decrypt(privateKey, content);
    }

    private DiaryDetailInfo getDiaryDetailInfo(Long memberId, Long diaryId) {
        return diaryRepository.findDiaryDetailInfo(memberId, diaryId)
                .orElseThrow(() -> new DiaryNotFoundException(ErrorCode.DIARY_NOT_FOUND, diaryId));
    }
}
