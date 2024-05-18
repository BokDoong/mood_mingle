package uni.capstone.moodmingle.config.security.oidc.utils;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.config.security.exception.ExpiredTokenException;
import uni.capstone.moodmingle.config.security.exception.InvalidTokenException;

/**
 * ID 토큰 검증하는 클래스
 */
@Component
public class OidcTokenVerifier {

    /**
     * TODO: 다른 소셜 서버와의 연동 및 확장성 고려
     * Kakao AUD, ISS 값
     */
    @Value("${social-server.kakao.aud}")
    private String aud;
    @Value("${social-server.kakao.iss}")
    private String iss;

    /**
     * ID_Token 검증
     *
     * @param token ID_Token
     * @return
     */
    public void verifyTokenInfos(String token) {
        try {
            String payload = parsePayloadFromToken(token);
            Jwts.parserBuilder()
                    .requireAudience(aud)
                    .requireIssuer(iss)
                    .build()
                    .parseClaimsJwt(payload);
        } catch (ExpiredJwtException ex) {
            throw new ExpiredTokenException("만료된 토큰인 경우");     // 토큰 만료
        } catch (Exception ex) {
            throw new InvalidTokenException("유효하지 않은 토큰인 경우");      // 유효하지 않은 토큰
        }
    }

    /**
     * 토큰에서 페이로드 추출
     *
     * @param token 토큰
     * @return 추출된 페이로드
     */
    private String parsePayloadFromToken(String token) {
        String[] splitToken = token.split("\\.");

        if (splitToken.length != 3) {
            throw new InvalidTokenException("유효하지 않은 ID_Token 형식인 경우");
        }
        return splitToken[0] + "." + splitToken[1] + ".";
    }
}
