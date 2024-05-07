package uni.capstone.moodmingle.diary.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uni.capstone.moodmingle.diary.application.dto.response.DiaryDetailInfo;
import uni.capstone.moodmingle.diary.application.dto.response.DiaryInfo;
import uni.capstone.moodmingle.diary.domain.DiaryRepository;
import uni.capstone.moodmingle.exception.NotFoundException;
import uni.capstone.moodmingle.exception.code.ErrorCode;

import java.time.LocalDate;
import java.util.List;

/**
 * Diary 도메인 조회 로직 담당하는 애플리케이션
 *
 * @author ijin
 */
@Service
@RequiredArgsConstructor
public class DiaryQueryService {

    private final DiaryRepository diaryRepository;

    /**
     * 월별 일기 조회
     *
     * @param memberId Member's Id
     * @param date 년&월
     * @return DiaryInfos
     */
    @Transactional(readOnly = true)
    public List<DiaryInfo> findMonthlyDiaryInfos(Long memberId, LocalDate date) {
        return diaryRepository.findMonthlyDiaryInfos(memberId, date);
    }

    @Transactional(readOnly = true)
    public DiaryDetailInfo findDiaryDetailInfo(Long memberId, Long diaryId) {
        return diaryRepository.findDiaryDetailInfo(memberId, diaryId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DIARY_NOT_FOUND, diaryId));
    }
}
