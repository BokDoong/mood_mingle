package uni.capstone.moodmingle.global.security.jwt.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.global.security.exception.ParsingRequestedTokenException;

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

    // JWT Token -> 유저 ID
    public Long extractUserId(String token) {
        return parseClaims(token).get("userId", Long.class);
    }

    // HTTP Request -> 유저 ID
    public Long extractUserId(HttpServletRequest request) {
        String token = extractTokenFromHeader(request);
        return parseClaims(token).get("userId", Long.class);
    }

    // HTTP Request -> JWT 토큰
    public String extractTokenFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        } else {
            throw new ParsingRequestedTokenException("Http 요청 Access Token 이 비어 있거나 Bearer 형식 토큰이 아닌 경우");
        }
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
