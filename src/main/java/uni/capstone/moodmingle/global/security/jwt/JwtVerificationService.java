package uni.capstone.moodmingle.global.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uni.capstone.moodmingle.global.security.jwt.utils.JwtExtractor;
import uni.capstone.moodmingle.global.security.jwt.utils.JwtTokenManager;
import uni.capstone.moodmingle.global.security.jwt.utils.JwtVerifier;

@Service
@RequiredArgsConstructor
public class JwtVerificationService {

    private final JwtExtractor jwtExtractor;
    private final JwtVerifier jwtVerifier;
    private final JwtTokenManager jwtTokenManager;

    // 유저 ID 추출
    public Long extractUserId(String token) {
        return jwtExtractor.extractUserId(token);
    }

    // 리프레쉬 토큰 검증
    public void verifyRefreshToken(String refreshToken) {
        jwtVerifier.verifyRefreshToken(extractUserId(refreshToken), refreshToken);
    }

    // 리프레쉬 토큰 만료
    public void expireRefreshToken(Long memberId) {
        jwtTokenManager.expireRefreshToken(memberId);
    }
}
