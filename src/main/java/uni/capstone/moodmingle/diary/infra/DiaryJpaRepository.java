package uni.capstone.moodmingle.diary.infra;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uni.capstone.moodmingle.diary.domain.Diary;
import uni.capstone.moodmingle.diary.domain.DiaryRepository;

/**
 * DB 와 JPA 로 접근하는 일기 리포지토리 구현체
 *
 * @author ijin
 */
@Repository
@RequiredArgsConstructor
public class DiaryJpaRepository implements DiaryRepository {

    private final EntityManager em;

    /**
     * 저장
     *
     * @param diary Diary 객체
     */
    @Override
    public void save(Diary diary) {
        em.persist(diary);
    }
}
