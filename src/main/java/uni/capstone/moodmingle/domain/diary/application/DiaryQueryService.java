package uni.capstone.moodmingle.domain.diary.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uni.capstone.moodmingle.domain.diary.application.dto.response.DiaryDetailInfo;
import uni.capstone.moodmingle.domain.diary.application.dto.response.DiaryInfo;
import uni.capstone.moodmingle.domain.diary.domain.Diary;
import uni.capstone.moodmingle.domain.diary.domain.DiaryRepository;
import uni.capstone.moodmingle.domain.diary.domain.EmotionCalculator;
import uni.capstone.moodmingle.domain.diary.exception.DiaryNotFoundException;
import uni.capstone.moodmingle.domain.member.application.MemberQueryService;
import uni.capstone.moodmingle.domain.member.application.dto.response.SecretInfos;
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

    private final DiaryCryptoHelper cryptoHelper;
    private final EmotionCalculator emotionCalculator;

    // 월별 일기 조회
    @Transactional(readOnly = true)
    public List<DiaryInfo> findMonthlyDiaryInfos(Long memberId, LocalDate date) {
        return diaryRepository.findMonthlyDiaryInfos(memberId, date);
    }

    // 일기 상세 조회
    @Transactional(readOnly = true)
    public DiaryDetailInfo findDiaryDetailInfo(Long memberId, Long diaryId) {
        DiaryDetailInfo diaryDetailInfo = getDiaryDetailInfo(memberId, diaryId);
        SecretInfos secretInfos = memberQueryService.findMemberSecretInfos(memberId);
        return setDecryptedInfos(diaryDetailInfo, secretInfos);
    }

    // 월별 감정 통계 조회
    public HashMap<String, Integer> MonthlyEmotionsInfo(Long memberId, LocalDate date) {
        List<Diary.Emotion> monthlyEmotions = diaryRepository.findMonthlyEmotionsInfo(memberId, date);
        return emotionCalculator.makeStatisticsOfEmotions(monthlyEmotions);
    }

    private DiaryDetailInfo setDecryptedInfos(DiaryDetailInfo diaryDetailInfo, SecretInfos secretInfos) {
        diaryDetailInfo.setDecryptedDiaryContent(getDecryptedContent(secretInfos, diaryDetailInfo.getContent()));
        if (!diaryDetailInfo.verifyReplyContentEmpty()) {
            diaryDetailInfo.setDecryptedReplyContent(getDecryptedContent(secretInfos, diaryDetailInfo.getReplyContent()));
        }
        return diaryDetailInfo;
    }

    private String getDecryptedContent(SecretInfos secretInfos, String content) {
        return cryptoHelper.decryptContent(secretInfos, content);
    }

    private DiaryDetailInfo getDiaryDetailInfo(Long memberId, Long diaryId) {
        return diaryRepository.findDiaryDetailInfo(memberId, diaryId)
                .orElseThrow(() -> new DiaryNotFoundException(ErrorCode.DIARY_NOT_FOUND, diaryId));
    }
}
