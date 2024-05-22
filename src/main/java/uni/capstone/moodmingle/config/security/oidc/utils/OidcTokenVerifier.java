package uni.capstone.moodmingle.config.security.oidc.utils;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
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
     * 각 소셜의 AUD, ISS 값
     */
    @Value("${social-server.kakao.aud}")
    private String kakaoAud;
    @Value("${social-server.kakao.iss}")
    private String kakaoIss;
    @Value("${social-server.apple.aud}")
    private String appleAud;
    @Value("${social-server.apple.iss}")
    private String appleIss;

    /**
     * ID_Token 검증
     *
     * @param token ID_Token
     * @return
     */
    public void verifyTokenInfos(String authServer, String token) {
        try {
            String payload = parsePayloadFromToken(token);
            switch (authServer) {
                case "kakao" -> parseAndVerifyToken(kakaoAud, kakaoIss, payload);
                case "apple" -> parseAndVerifyToken(appleAud, appleIss, payload);
            }
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

    /**
     * Jwt 토큰 파싱 및 검증
     *
     * @param aud
     * @param iss
     * @param payload
     */
    private void parseAndVerifyToken(String aud, String iss, String payload) {
        Jwts.parserBuilder()
                .requireAudience(aud)
                .requireIssuer(iss)
                .build()
                .parseClaimsJwt(payload);
    }
}
