package uni.capstone.moodmingle.member.infra;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import uni.capstone.moodmingle.member.domain.Member;
import uni.capstone.moodmingle.member.domain.MemberRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository implements MemberRepository {

    private final EntityManager em;

    @Override
    public void save(Member member) {
        em.persist(member);
    }

    @Override
    public Optional<Member> findById(long memberId) {
        return Optional.ofNullable(em.find(Member.class, memberId));
    }
}
