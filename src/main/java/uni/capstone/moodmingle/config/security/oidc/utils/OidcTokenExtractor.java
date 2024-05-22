package uni.capstone.moodmingle.config.security.oidc.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.config.security.oidc.entity.OidcUserInfo;

/**
 * 토큰에서 값 추출
 */
@Component
public class OidcTokenExtractor {

    private final String KID = "kid";

    /**
     * 요청된 토큰의 Kid 값 추출
     *
     * @param token 토크
     * @return Kid 값
     */
    public String extractKidValue(String token) {
        return (String) getUnsignedTokenClaims(token).getHeader().get(KID);
    }

    /**
     * 디코딩된 Body 에서 유저 정보 추출
     *
     * @param body Claim's Body
     * @return UserInfo
     */
    public OidcUserInfo extractUserInfo(String authServer, Claims body) {
        switch (authServer) {
            case "kakao" -> {
                return getKakaoOidcUserInfo(body);
            }
            case "apple" -> {
                return getAppleOidcUserInfo(body);
            }
            default -> throw new IllegalStateException("Unexpected value: " + authServer);
        }
    }

    private OidcUserInfo getAppleOidcUserInfo(Claims body) {
        String email = body.get("email", String.class);
        return new OidcUserInfo(null, email, null);
    }

    private OidcUserInfo getKakaoOidcUserInfo(Claims body) {
        String email = body.get("email", String.class);
        String nickname = body.get("nickname", String.class);
        String picture = body.get("picture", String.class);
        return new OidcUserInfo(nickname, email, picture);
    }

    private Jwt<Header, Claims> getUnsignedTokenClaims(String token) {
        String payload = parsePayloadFromToken(token);
        return Jwts.parserBuilder()
                .build()
                .parseClaimsJwt(payload);
    }

    private String parsePayloadFromToken(String token) {
        String[] splitToken = token.split("\\.");
        return splitToken[0] + "." + splitToken[1] + ".";
    }
}
