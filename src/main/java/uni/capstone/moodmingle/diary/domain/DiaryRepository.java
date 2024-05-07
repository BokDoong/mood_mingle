package uni.capstone.moodmingle.diary.domain;

import java.util.Optional;

/**
 * Diary Repository
 */
public interface DiaryRepository {

    /**
     * 저장
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

    void saveReply(Reply reply);
}
