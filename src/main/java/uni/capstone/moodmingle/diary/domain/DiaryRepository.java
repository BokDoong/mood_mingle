package uni.capstone.moodmingle.diary.domain;

/**
 * Diary Repository
 */
public interface DiaryRepository {

    /**
     * 저장
     *
     * @param diary Diary 객짜
     */
    void save(Diary diary);

    
}
