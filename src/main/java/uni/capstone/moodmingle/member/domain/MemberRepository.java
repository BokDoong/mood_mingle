package uni.capstone.moodmingle.member.domain;

import java.util.Optional;

/**
 * Member 리포지토리
 *
 * @author ijin
 */
public interface MemberRepository {

    /**
     * 저장
     *
     * @param member
     */
    void save(Member member);

    /**
     * ID 로 검색
     *
     * @param memberId ID
     * @return Member
     */
    Optional<Member> findById(long memberId);

    /**
     * Email 로 검색
     *
     * @param email 이메일
     * @return Member
     */
    Optional<Long> findMemberIdByEmail(String email);
}
