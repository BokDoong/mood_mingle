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
     * SecretInfo 저장
     *
     * @param secretInfo
     */
    void save(MemberSecretInfo secretInfo);

    /**
     * ID 로 검색
     *
     * @param memberId ID
     * @return Member
     */
    Optional<Member> findById(long memberId);

    /**
     * ID 로 검색
     *
     * @param memberId ID
     * @return  MemberSecretInfo
     */
    Optional<MemberSecretInfo> findSecretInfoById(long memberId);

    /**
     * Email 로 검색
     *
     * @param email 이메일
     * @return Member
     */
    Optional<Long> findMemberIdByEmail(String email);

    /**
     * 회원 삭제
     *
     * @param memberId
     */
    void deleteMember(long memberId);
}
