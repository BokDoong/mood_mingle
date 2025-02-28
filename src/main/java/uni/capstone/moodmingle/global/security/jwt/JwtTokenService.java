package uni.capstone.moodmingle.global.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uni.capstone.moodmingle.global.security.jwt.factory.JwtFactory;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtFactory jwtFactory;

    // 액세스 토큰 생성
    public String createAccessToken(Long memberId) {
        return jwtFactory.createAccessToken(memberId);
    }

    // 리프레쉬 토큰 생성
    public String createRefreshToken(Long memberId) {
        return jwtFactory.createRefreshToken(memberId);
    }
}
