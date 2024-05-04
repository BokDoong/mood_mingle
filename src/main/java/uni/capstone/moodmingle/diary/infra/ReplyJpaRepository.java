package uni.capstone.moodmingle.diary.infra;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uni.capstone.moodmingle.diary.domain.Diary;
import uni.capstone.moodmingle.diary.domain.DiaryRepository;

@Repository
@RequiredArgsConstructor
public class ReplyJpaRepository implements DiaryRepository {

    private final EntityManager em;

    @Override
    public void save(Diary diary) {
        em.persist(diary);
    }
}
