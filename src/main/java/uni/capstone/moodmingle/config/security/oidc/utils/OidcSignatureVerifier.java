package uni.capstone.moodmingle.config.security.oidc.utils;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.config.security.exception.ExpiredTokenException;
import uni.capstone.moodmingle.config.security.exception.InvalidTokenException;

import java.security.Key;

/**
 * 공개키로 서명 검증
 */
@Component
public class OidcSignatureVerifier {

    /**
     * 서명 검증
     *
     * @param token 토큰
     * @param publicKey 공개키
     * @return 해독한 토큰의 클레임
     */
    public Jws<Claims> verifyAndGetOidcTokenJws(String token, Key publicKey) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("JWT 토큰이 아닌 경우");       // JWT 형식이 아닌 경우
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException("만료된 토큰인 경우");      // 토큰 만료
        } catch (Exception e) {
            throw new InvalidTokenException("토큰 인증 실패했을 경우");    // 토큰 인증 실패
        }
    }
}
