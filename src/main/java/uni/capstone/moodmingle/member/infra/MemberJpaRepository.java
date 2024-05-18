package uni.capstone.moodmingle.member.infra;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uni.capstone.moodmingle.member.domain.Member;
import uni.capstone.moodmingle.member.domain.MemberRepository;

import java.util.List;
import java.util.Optional;

/**
 * JPA 를 이용한 리포지토리 구현체
 *
 * @author ijin
 */
@Repository
@RequiredArgsConstructor
public class MemberJpaRepository implements MemberRepository {

    private final EntityManager em;

    /**
     * 저장
     *
     * @param member
     */
    @Override
    public void save(Member member) {
        em.persist(member);
    }

    /**
     * ID 로 검색
     *
     * @param memberId ID
     * @return Member
     */
    @Override
    public Optional<Member> findById(long memberId) {
        return Optional.ofNullable(em.find(Member.class, memberId));
    }

    /**
     * Email 로 검색
     *
     * @param email 이메일
     * @return Member
     */
    @Override
    public Optional<Long> findMemberIdByEmail(String email) {
        List<Long> memberIds =em.createQuery(
                "select m.id" +
                        " from Member m" +
                        " where m.email = :email", Long.class)
                .setParameter("email", email)
                .getResultList();
        return memberIds.stream().findAny();
    }
}
