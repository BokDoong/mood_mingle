package uni.capstone.moodmingle.domain.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uni.capstone.moodmingle.domain.member.application.dto.response.MemberInfo;
import uni.capstone.moodmingle.domain.member.domain.Member;
import uni.capstone.moodmingle.domain.member.domain.MemberRepository;
import uni.capstone.moodmingle.domain.member.exception.MemberNotFoundException;
import uni.capstone.moodmingle.global.error.ErrorCode;

/**
 * Member 도메인 조회 응용 서비스
 *
 * @author ijin
 */
@Service
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberRepository memberRepository;

    /**
     * memberId => 회원 조회
     *
     * @param memberId 멤버 ID
     * @return  Member
     */
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND, memberId));
    }

    /**
     * memberId => 회원정보 조회
     *
     * @param memberId 멤버 ID
     * @return MemberInfo DTO
     */
    public MemberInfo findMemberInfo(Long memberId) {
        Member member = findMemberById(memberId);
        return toMemberInfo(member);
    }

    private MemberInfo toMemberInfo(Member member) {
        return new MemberInfo(member.getName(), member.getEmail(), member.getImageUrl());
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND, memberId));
    }
}
