package uni.capstone.moodmingle.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uni.capstone.moodmingle.config.security.jwt.factory.JwtFactory;
import uni.capstone.moodmingle.config.security.jwt.utils.JwtExtractor;
import uni.capstone.moodmingle.config.security.jwt.utils.JwtTokenManager;
import uni.capstone.moodmingle.config.security.jwt.utils.JwtVerifier;

/**
 * JWT 로직 서비스
 *
 * @author ijin
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtFactory jwtFactory;
    private final JwtExtractor jwtExtractor;
    private final JwtVerifier jwtVerifier;
    private final JwtTokenManager jwtTokenManager;

    /**
     * 액세스 토큰 생성
     *
     * @param memberId 멤버 ID
     * @return 액세스 토큰
     */
    public String createAccessToken(Long memberId) {
        return jwtFactory.createAccessToken(memberId);
    }

    /**
     * 리프레쉬 토큰 생성
     *
     * @param memberId 멤버 ID
     * @return 리프레쉬 토큰
     */
    public String createRefreshToken(Long memberId) {
        return jwtFactory.createRefreshToken(memberId);
    }

    /**
     * 액세스 토큰 -> 유저 ID 추출
     *
     * @param token 토큰
     * @return      유저 ID
     */
    public Long extractUserId(String token) {
        return jwtExtractor.extractUserId(token);
    }

    /**
     * 리프레쉬 토큰 검증
     *
     * @param refreshToken  리프레쉬 토큰
     */
    public void verifyRefreshToken(String refreshToken) {
        jwtVerifier.verifyRefreshToken(extractUserId(refreshToken), refreshToken);
    }

    /**
     * 리프레쉬 토큰 만료됐는지 검사
     *
     * @param memberId  멤버 ID
     */
    public void expireRefreshToken(Long memberId) {
        jwtTokenManager.expireRefreshToken(memberId);
    }
}
