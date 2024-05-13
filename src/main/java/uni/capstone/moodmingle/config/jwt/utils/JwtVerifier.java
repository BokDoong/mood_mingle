package uni.capstone.moodmingle.config.jwt.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.config.jwt.JwtException;
import uni.capstone.moodmingle.exception.BusinessException;
import uni.capstone.moodmingle.exception.NotFoundException;
import uni.capstone.moodmingle.exception.code.ErrorCode;

import java.security.Key;
import java.util.Optional;

/**
 * 토큰 유효성 검사
 *
 * @author ijin
 */
@Component
public class JwtVerifier {

    /**
     * RedisTemplate - 레디스에서 리프레쉬 토큰 정보를 갖고오기 위한 Template
     * Key - 토큰 암호화&해독을 위한 Key
     */
    private final Key secretKey;
    private final RedisTemplate<String, String> redisTemplate;

    public JwtVerifier(
            @Qualifier("JwtTokenRedisTemplate") RedisTemplate<String, String> redisTemplate,
            @Value("${jwt.secret}") String secretKey
    ) {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.redisTemplate = redisTemplate;
    }

    /**
     * 토큰 유효성 검사
     *
     * @param token 토큰
     * @param request HTTP 요청
     * @return 검증 결과
     */
    public boolean validateToken(String token, HttpServletRequest request) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;    // 성공
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException |
                 SignatureException e) {
            setAttribute(request, JwtException.WRONG_TOKEN);    // 토큰 인증 실패
        } catch (ExpiredJwtException e) {
            setAttribute(request, JwtException.EXPIRED_TOKEN);      // 토큰 만료
        }
        return false;
    }

    /**
     * Redis 에서 리프레쉬 토큰을 조회하여 유효한지 확인
     *
     * @param userId 유저 ID
     * @param refreshToken 리프레쉬 토큰
     */
    public void verifyRefreshToken(long userId, String refreshToken) {
        String storedRefreshToken = findRefreshToken(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.INVALID_REFRESH_TOKEN));
        if(!storedRefreshToken.equals(refreshToken))
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
    }

    /**
     * Redis 에서 리프레쉬 토큰 삭제
     *
     * @param userId 유저 ID
     */
    public void expireRefreshToken(long userId) {
        redisTemplate.delete(String.valueOf(userId));
    }

    private Optional<String> findRefreshToken(long userId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(String.valueOf(userId)));
    }


    private void setAttribute(HttpServletRequest request, JwtException exception) {
        request.setAttribute("exception", exception.name());
    }
}
