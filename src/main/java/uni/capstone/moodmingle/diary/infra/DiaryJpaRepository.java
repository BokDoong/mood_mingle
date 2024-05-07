package uni.capstone.moodmingle.diary.infra;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uni.capstone.moodmingle.diary.domain.Diary;
import uni.capstone.moodmingle.diary.domain.DiaryRepository;
import uni.capstone.moodmingle.diary.domain.Reply;
import uni.capstone.moodmingle.member.domain.Member;

import java.util.Optional;

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
    public void saveDiary(Diary diary) {
        em.persist(diary);
    }

    /**
     * ID 로 찾기
     *
     * @param diaryId Diary's Id
     * @return 찾은 Diary 객체
     */
    @Override
    public Optional<Diary> findById(long diaryId) {
        return Optional.ofNullable(em.find(Diary.class, diaryId));
    }

    @Override
    public void saveReply(Reply reply) {
        em.persist(reply);
    }
}
