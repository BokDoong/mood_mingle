package uni.capstone.moodmingle.diary.domain;

import uni.capstone.moodmingle.diary.application.dto.response.DiaryInfo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Diary Repository
 */
public interface DiaryRepository {

    /**
     * 일기 저장
     *
     * @param diary Diary 객체
     */
    void saveDiary(Diary diary);

    /**
     * Diary 찾기
     *
     * @param diaryId Diary's Id
     * @return Diary 객체
     */
    Optional<Diary> findById(long diaryId);

    /**
     * 해당 날짜에 일기가 이미 있는지 조회
     *
     * @param memberId Member's Id
     * @param date 날짜
     * @return 존재 유무
     */
    boolean checkDiaryAlreadyExist(Long memberId, LocalDate date);

    /**
     * 답장 저장
     *
     * @param reply Reply 객체
     */
    void saveReply(Reply reply);

    /**
     * 월별 일기 조회
     *
     * @param memberId Member's Id
     * @param date 년도&월
     * @return  DiaryInfos
     */
    List<DiaryInfo> findMonthlyDiaryInfos(Long memberId, LocalDate date);
}
