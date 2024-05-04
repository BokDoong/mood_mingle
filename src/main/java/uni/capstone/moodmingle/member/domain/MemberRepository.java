package uni.capstone.moodmingle.member.domain;

import java.util.Optional;

public interface MemberRepository {

    void save(Member member);

    Optional<Member> findById(long memberId);
}
