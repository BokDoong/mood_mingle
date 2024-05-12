package uni.capstone.moodmingle.diary.infra;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uni.capstone.moodmingle.diary.application.dto.response.DiaryDetailInfo;
import uni.capstone.moodmingle.diary.application.dto.response.DiaryInfo;
import uni.capstone.moodmingle.diary.domain.Diary;
import uni.capstone.moodmingle.diary.domain.DiaryRepository;
import uni.capstone.moodmingle.diary.domain.Reply;
import uni.capstone.moodmingle.exception.NotFoundException;
import uni.capstone.moodmingle.exception.code.ErrorCode;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static uni.capstone.moodmingle.diary.domain.Diary.*;

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

    /**
     * 작성하려는 날짜에 일기가 존재하는지 체크
     *
     * @param memberId Member's Id
     * @param date 날짜
     * @return 존재 유무
     */
    @Override
    public boolean checkDiaryAlreadyExist(Long memberId, LocalDate date) {
        return em.createQuery(
                        "select count(d.id) > 0" +
                                " from Diary d" +
                                " join d.member m" +
                                " where m.id = :memberId" +
                                " and d.date = :date", Boolean.class)
                .setParameter("memberId", memberId)
                .setParameter("date", date)
                .getSingleResult();
    }

    /**
     * 답장 저장
     *
     * @param reply Reply 객체
     */
    @Override
    public void saveReply(Reply reply) {
        em.persist(reply);
    }

    /**
     * 조회하는 월의 일기 조회
     *
     * @param memberId Member's Id
     * @param date 년도&월
     * @return DiaryInfos
     */
    @Override
    public List<DiaryInfo> findMonthlyDiaryInfos(Long memberId, LocalDate date) {
        LocalDate startDate = date.withDayOfMonth(1); // 해당 월의 시작일
        LocalDate endDate = date.withDayOfMonth(date.lengthOfMonth()); // 해당 월의 마지막 날

        return em.createQuery(
                        "select new uni.capstone.moodmingle.diary.application.dto.response.DiaryInfo(d.id, d.date, d.emotion)" +
                                " from Diary d" +
                                " join d.member m" +
                                " where m.id = :memberId" +
                                " and d.date >= :startDate and d.date <= :endDate" +
                                " order by d.date asc", DiaryInfo.class)
                .setParameter("memberId", memberId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }

    /**
     * 일기 상세조회
     *
     * @param memberId
     * @param diaryId
     * @return
     */
    @Override
    public Optional<DiaryDetailInfo> findDiaryDetailInfo(Long memberId, Long diaryId) {
        List<DiaryDetailInfo> detailInfos = em.createQuery(
                        "select new uni.capstone.moodmingle.diary.application.dto.response.DiaryDetailInfo" +
                                "(d.id, d.title, d.content, d.date, d.emotion, d.weather, d.image.imageUrl, r.content, r.type)" +
                                " from Diary d" +
                                " join d.member m" +
                                " left join d.reply r" +
                                " where m.id = :memberId and d.id = :diaryId", DiaryDetailInfo.class)
                .setParameter("memberId", memberId)
                .setParameter("diaryId", diaryId)
                .getResultList();
        return detailInfos.stream().findAny();
    }

    @Override
    public List<Emotion> findMonthlyEmotionsInfo(Long memberId, LocalDate date) {
        LocalDate startDate = date.withDayOfMonth(1); // 해당 월의 시작일
        LocalDate endDate = date.withDayOfMonth(date.lengthOfMonth()); // 해당 월의 마지막 날

        return em.createQuery(
                        "select d.emotion" +
                                " from Diary d" +
                                " join d.member m" +
                                " where m.id = :memberId" +
                                " and d.date >= :startDate and d.date <= :endDate" +
                                " order by d.date asc", Emotion.class)
                .setParameter("memberId", memberId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }
}
