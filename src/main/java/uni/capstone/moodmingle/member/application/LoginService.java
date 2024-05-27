package uni.capstone.moodmingle.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uni.capstone.moodmingle.exception.BusinessException;
import uni.capstone.moodmingle.exception.NotFoundException;
import uni.capstone.moodmingle.exception.code.ErrorCode;
import uni.capstone.moodmingle.member.application.dto.MemberCommandMapper;
import uni.capstone.moodmingle.member.application.dto.request.MemberCreateCommand;
import uni.capstone.moodmingle.member.application.dto.response.TokenResponse;
import uni.capstone.moodmingle.member.domain.Member;
import uni.capstone.moodmingle.member.domain.MemberRepository;
import uni.capstone.moodmingle.member.domain.MemberSecretInfo;

/**
 * 로그인 서비스
 *
 * @author ijin
 */
@Service
@RequiredArgsConstructor
public class LoginService {

    private final JwtService jwtService;
    private final MemberCryptoHelper memberCryptoHelper;
    private final MemberRepository memberRepository;
    private final MemberCommandMapper mapper;

    /**
     * 회원가입
     * TODO: Sub DB 에 시크릿 키 저장
     *
     * @param command 멤버 생성 DTO
     * @return 액세스 토큰 + 리프레쉬 토큰
     */
    @Transactional
    public TokenResponse register(MemberCreateCommand command) {
        Member member = createAndSaveMember(command);
        createAndSaveSecretInfo(member);
        return toTokenResponse(member.getId());
    }

    /**
     * 카카오 로그인: Kakao Email => 회원 존재 유무 검사 => 없으면 새로
     *
     * @return 액세스 토큰 + 리프레쉬 토큰
     */
    @Transactional
    public TokenResponse kakaoLogin(String email) {
        // 기존 회원 검즘
        Long memberId = findMemberId(email);
        // 토큰 발급
        return toTokenResponse(memberId);
    }

    /**
     * TODO: 애플 로그인도 회원가입, 로그인 로직 분리
     * 애플 로그인 및 회원가입: Apple Email => 회원 존재 유무 검사 => 없으면 저장
     *
     * @return 액세스 토큰 + 리프레쉬 토큰
     */
    @Transactional
    public TokenResponse appleLogin(String email, String name) {
        try {
            Long memberId = findMemberId(email);
            return toTokenResponse(memberId);  // 토큰 발급
        } catch (NotFoundException ex) {
            Member member = createAndSaveAppleAccountMember(name, email);
            createAndSaveSecretInfo(member);
            return toTokenResponse(member.getId());  // 토큰 발급
        }
    }

    /**
     * 토큰 재발급
     *
     * @param refreshToken 리프레쉬 토큰
     * @return 재발급된 액세스 토큰 + 리프레쉬 토큰
     */
    @Transactional
    public TokenResponse reissue(String refreshToken) {
        verifyRefreshTokenExist(refreshToken);
        return toTokenResponse(extractMemberIdFromToken(refreshToken));
    }

    /**
     * 로그아웃
     *
     * @param memberId 멤버 ID
     */
    @Transactional
    public void logout(long memberId) {
        verifyMemberExist(memberId);
        expireUsedRefreshToken(memberId);
    }

    /**
     * 회원 탈퇴
     *
     * @param memberId 멤버 ID
     */
    @Transactional
    public void withdraw(long memberId) {
        verifyMemberExist(memberId);
        expireUsedRefreshToken(memberId);
        deleteMember(memberId);
    }

    private void expireUsedRefreshToken(long memberId) {
        jwtService.expireRefreshToken(memberId);
    }

    private Long extractMemberIdFromToken(String refreshToken) {
        return jwtService.extractUserId(refreshToken);
    }

    private void verifyRefreshTokenExist(String refreshToken) {
        jwtService.verifyRefreshToken(refreshToken);
    }

    private MemberSecretInfo createAndSaveSecretInfo(Member member) {
        MemberSecretInfo secretInfo = memberCryptoHelper.createSecretInfo(member.getId());
        memberRepository.save(secretInfo);
        return secretInfo;
    }

    private Member createAndSaveAppleAccountMember(String name, String email) {
        Member member = mapper.toMember(name, email);
        saveMember(member);
        return member;
    }

    private void deleteMember(long memberId) {
        memberRepository.deleteMember(memberId);
    }

    private Member createAndSaveMember(MemberCreateCommand command) {
        Member member = mapper.toMember(command);
        saveMember(member);
        return member;
    }

    private void saveMember(Member member) {
        try {
            memberRepository.save(member);
        } catch (DataIntegrityViolationException exception) {
            throw new BusinessException(ErrorCode.MEMBER_ALREADY_EXISTED);
        }
    }

    private Member verifyMemberExist(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND, memberId));
    }

    private TokenResponse toTokenResponse(long memberId) {
        return TokenResponse.builder()
                .accessToken(jwtService.createAccessToken(memberId))
                .refreshToken(jwtService.createRefreshToken(memberId))
                .build();
    }

    private Long findMemberId(String email) {
        return memberRepository.findMemberIdByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
