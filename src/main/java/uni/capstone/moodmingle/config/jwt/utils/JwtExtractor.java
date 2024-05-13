package uni.capstone.moodmingle.config.jwt.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

/**
 * 토큰에서 유저 정보 추출
 *
 * @author ijin
 */
@Component
public class JwtExtractor {

    /**
     * 해독을 위해 사용할 JWT Key
     */
    private final Key key;

    public JwtExtractor(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * JWT Token -> 유저 ID 추출
     *
     * @param token Client 로부터 받는 토큰
     * @return UserID
     */
    public Long extractUserId(String token) {
        return parseClaims(token).get("userId", Long.class);
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
