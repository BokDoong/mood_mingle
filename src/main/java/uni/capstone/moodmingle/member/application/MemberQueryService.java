package uni.capstone.moodmingle.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uni.capstone.moodmingle.exception.NotFoundException;
import uni.capstone.moodmingle.exception.code.ErrorCode;
import uni.capstone.moodmingle.member.application.dto.response.MemberInfo;
import uni.capstone.moodmingle.member.domain.Member;
import uni.capstone.moodmingle.member.domain.MemberRepository;

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
     * memberId => 회원정보 조회
     *
     * @param memberId
     * @return MemberInfo DTO
     */
    public MemberInfo findMemberInfo(Long memberId) {
        Member member = findMemberById(memberId);
        return new MemberInfo(member.getName(), member.getEmail(), member.getImageUrl());
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND, memberId));
    }
}
