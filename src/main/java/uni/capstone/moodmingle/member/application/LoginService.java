package uni.capstone.moodmingle.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uni.capstone.moodmingle.config.security.jwt.factory.JwtFactory;
import uni.capstone.moodmingle.config.security.jwt.utils.JwtExtractor;
import uni.capstone.moodmingle.config.security.jwt.utils.JwtTokenManager;
import uni.capstone.moodmingle.config.security.jwt.utils.JwtVerifier;
import uni.capstone.moodmingle.exception.BusinessException;
import uni.capstone.moodmingle.exception.NotFoundException;
import uni.capstone.moodmingle.exception.code.ErrorCode;
import uni.capstone.moodmingle.member.application.dto.MemberCommandMapper;
import uni.capstone.moodmingle.member.application.dto.request.MemberCreateCommand;
import uni.capstone.moodmingle.member.application.dto.response.TokenResponse;
import uni.capstone.moodmingle.member.domain.Member;
import uni.capstone.moodmingle.member.domain.MemberRepository;

/**
 * 인증 도메인 서비스
 *
 * @author ijin
 */
@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final MemberCommandMapper mapper;
    private final JwtFactory jwtFactory;
    private final JwtExtractor jwtExtractor;
    private final JwtVerifier jwtVerifier;
    private final JwtTokenManager jwtTokenManager;

    /**
     * 회원가입
     *
     * @param command 멤버 생성 DTO
     * @return 액세스 토큰 + 리프레쉬 토큰
     */
    @Transactional
    public TokenResponse register(MemberCreateCommand command) {
        Member member = createAndSaveMember(command);
        return toTokenResponse(createAccessToken(member.getId()), createRefreshToken(member.getId()));
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
        return toTokenResponse(createAccessToken(memberId), createRefreshToken(memberId));
    }

    /**
     * 애플 로그인 및 회원가입: Apple Email => 회원 존재 유무 검사 => 없으면 저장
     *
     * @return 액세스 토큰 + 리프레쉬 토큰
     */
    @Transactional
    public TokenResponse appleLogin(String email, String name) {
        try {
            Long memberId = findMemberId(email);
            return toTokenResponse(createAccessToken(memberId), createRefreshToken(memberId));  // 토큰 발급
        } catch (NotFoundException ex) {
            Member member = createAppleAccountMember(name, email);
            saveMember(member);
            return toTokenResponse(createAccessToken(member.getId()), createRefreshToken(member.getId()));  // 토큰 발급
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
        // 토큰 -> Member Id 추출
        Long memberId = jwtExtractor.extractUserId(refreshToken);
        // 리프레쉬 토큰 검증
        jwtVerifier.verifyRefreshToken(memberId, refreshToken);
        // 토큰 재발급
        return toTokenResponse(createAccessToken(memberId), createRefreshToken(memberId));
    }

    /**
     * 로그아웃
     *
     * @param memberId 멤버 ID
     */
    @Transactional
    public void logout(long memberId) {
        verifyMemberExist(memberId);
        jwtTokenManager.expireRefreshToken(memberId);
    }

    /**
     * 회원 탈퇴
     *
     * @param memberId 멤버 ID
     */
    @Transactional
    public void withdraw(long memberId) {
        verifyMemberExist(memberId);
        jwtTokenManager.expireRefreshToken(memberId);
        deleteMember(memberId);
    }

    private Member createAppleAccountMember(String name, String email) {
        return mapper.toMember(name, email);
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

    private String createRefreshToken(Long memberId) {
        return jwtFactory.createRefreshToken(memberId);
    }

    private String createAccessToken(Long memberId) {
        return jwtFactory.createAccessToken(memberId);
    }

    private TokenResponse toTokenResponse(String accessToken, String refreshToken) {
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private Long findMemberId(String email) {
        return memberRepository.findMemberIdByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
