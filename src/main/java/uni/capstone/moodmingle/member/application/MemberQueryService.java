package uni.capstone.moodmingle.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uni.capstone.moodmingle.exception.BusinessException;
import uni.capstone.moodmingle.exception.NotFoundException;
import uni.capstone.moodmingle.exception.code.ErrorCode;
import uni.capstone.moodmingle.member.application.dto.response.MemberInfo;
import uni.capstone.moodmingle.member.application.dto.response.SecretInfos;
import uni.capstone.moodmingle.member.domain.Member;
import uni.capstone.moodmingle.member.domain.MemberRepository;
import uni.capstone.moodmingle.member.domain.MemberSecretInfo;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * Member 도메인 조회 응용 서비스
 *
 * @author ijin
 */
@Service
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberCryptoHelper memberCryptoHelper;
    private final MemberRepository memberRepository;

    /**
     * memberId => 회원 조회
     *
     * @param memberId 멤버 ID
     * @return  Member
     */
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND, memberId));
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

    /**
     * 비밀키, 초기 벡터 조회
     *
     * @param memberId 멤버 ID
     * @return SecretInfos DTO
     */
    public SecretInfos findMemberSecretInfos(Long memberId) {
        MemberSecretInfo memberSecretInfo = memberRepository.findSecretInfoById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        return toSecretInfos(memberSecretInfo);
    }

    private SecretInfos toSecretInfos(MemberSecretInfo memberSecretInfo) {
        return new SecretInfos(getDecryptedSecretKey(memberSecretInfo), getDecryptedIv(memberSecretInfo));
    }

    private IvParameterSpec getDecryptedIv(MemberSecretInfo memberSecretInfo) {
        return memberCryptoHelper.decryptIv(memberSecretInfo.getIv());
    }

    private SecretKey getDecryptedSecretKey(MemberSecretInfo memberSecretInfo) {
        return memberCryptoHelper.decryptSecretKey(memberSecretInfo.getSecretKey());
    }

    private MemberInfo toMemberInfo(Member member) {
        return new MemberInfo(member.getName(), member.getEmail(), member.getImageUrl());
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND, memberId));
    }
}
