package uni.capstone.moodmingle.domain.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uni.capstone.moodmingle.clients.aws.kms.KmsCrypto;
import uni.capstone.moodmingle.domain.member.application.dto.MemberCommandMapper;
import uni.capstone.moodmingle.domain.member.application.dto.request.MemberCreateCommand;
import uni.capstone.moodmingle.domain.member.application.dto.response.TokenResponse;
import uni.capstone.moodmingle.domain.member.domain.Member;
import uni.capstone.moodmingle.domain.member.domain.MemberRepository;
import uni.capstone.moodmingle.domain.member.exception.MemberAlreadyExistException;
import uni.capstone.moodmingle.domain.member.exception.MemberNotFoundException;
import uni.capstone.moodmingle.global.error.ErrorCode;
import uni.capstone.moodmingle.global.security.jwt.JwtTokenService;
import uni.capstone.moodmingle.global.security.jwt.JwtVerificationService;

/**
 * 로그인 서비스
 *
 * @author ijin
 */
@Service
@RequiredArgsConstructor
public class MemberCommandService {

    private final JwtVerificationService jwtVerificationService;
    private final JwtTokenService jwtTokenService;
    private final MemberRepository memberRepository;
    private final MemberCommandMapper mapper;
    private final KmsCrypto kmsCrypto;

    // 회원 가입
    @Transactional
    public TokenResponse register(MemberCreateCommand command) {
        // 새로운 회원
        Member member = mapper.toMember(command);
        // 비밀키 생성 및 암호화
        generateAndEncryptPrivateKey(member);
        // 저장, 토큰 발급
        saveMember(member);
        return toTokenResponse(member.getId());
    }

    // 로그인
    @Transactional
    public TokenResponse login(String email) {
        // 기존 회원 검즘
        Long memberId = findMemberId(email);
        // 토큰 발급
        return toTokenResponse(memberId);
    }

    // 토큰 재발급
    public TokenResponse reissue(String refreshToken) {
        verifyRefreshTokenExist(refreshToken);
        return toTokenResponse(extractMemberIdFromToken(refreshToken));
    }

    // 로그아웃
    @Transactional
    public void logout(long memberId) {
        verifyMemberExist(memberId);
        expireUsedRefreshToken(memberId);
    }

    // 회원 탈퇴
    @Transactional
    public void withdraw(long memberId) {
        verifyMemberExist(memberId);
        expireUsedRefreshToken(memberId);
        deleteMember(memberId);
    }

    private void generateAndEncryptPrivateKey(Member member) {
        byte[] privateKey = member.generateUserPrivateKey();
        member.setEncryptedPrivateKey(kmsCrypto.encrypt(privateKey));
    }

    private void expireUsedRefreshToken(long memberId) {
        jwtVerificationService.expireRefreshToken(memberId);
    }

    private Long extractMemberIdFromToken(String refreshToken) {
        return jwtVerificationService.extractUserId(refreshToken);
    }

    private void verifyRefreshTokenExist(String refreshToken) {
        jwtVerificationService.verifyRefreshToken(refreshToken);
    }

    private void deleteMember(long memberId) {
        memberRepository.deleteMember(memberId);
    }

    private void saveMember(Member member) {
        try {
            memberRepository.save(member);
        } catch (DataIntegrityViolationException exception) {
            throw new MemberAlreadyExistException(ErrorCode.MEMBER_ALREADY_EXISTED);
        }
    }

    private Member verifyMemberExist(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND, memberId));
    }

    private TokenResponse toTokenResponse(long memberId) {
        return TokenResponse.builder()
                .accessToken(jwtTokenService.createAccessToken(memberId))
                .refreshToken(jwtTokenService.createRefreshToken(memberId))
                .build();
    }

    private Long findMemberId(String email) {
        return memberRepository.findMemberIdByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
