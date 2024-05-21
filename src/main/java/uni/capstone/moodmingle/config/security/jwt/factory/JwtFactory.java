package uni.capstone.moodmingle.config.security.jwt.factory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * JWT Token 을 생성, 저장하는 Factory
 *
 * @author ijin
 */
@Component
public class JwtFactory {

    private final RedisTemplate<String, String> redisTemplate;
    private final Key key;
    private final long accessTokenExpTime;
    private final long refreshTokenExpTime;

    public JwtFactory(
            @Qualifier("JwtTokenRedisTemplate") RedisTemplate<String, String> redisTemplate,
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration_time.access_token}") long accessTokenExpTime,
            @Value("${jwt.expiration_time.refresh_token}") long refreshTokenExpTime
    ) {
        this.redisTemplate = redisTemplate;
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
        this.refreshTokenExpTime = refreshTokenExpTime;
    }

    /**
     * 액세스 토큰 생성
     *
     * @param memberId 유저 ID
     * @return 생성된 토큰
     */
    public String createAccessToken(long memberId) {
        // Claims 생성
        Claims claims = createClaims(memberId);
        ZonedDateTime now = ZonedDateTime.now();

        // 메타 데이터 포함 토큰 생성 및 리턴
        return createToken(claims, now, now.plusSeconds(accessTokenExpTime));
    }

    /**
     * 리프레쉬 토큰 생성
     *
     * @param memberId 유저 ID
     * @return 생성된 토큰
     */
    public String createRefreshToken(long memberId){
        // Claims 생성
        Claims claims = createClaims(memberId);
        ZonedDateTime now = ZonedDateTime.now();

        // 리프레쉬 토큰 생성 및 저장
        String refreshToken = createToken(claims, now, now.plusSeconds(refreshTokenExpTime));
        saveRefreshToken(memberId, refreshToken);

        return refreshToken;
    }

    private static Claims createClaims(long memberId) {
        Claims claims = Jwts.claims();
        claims.put("userId", memberId);
        return claims;
    }

    private String createToken(Claims claims, ZonedDateTime now, ZonedDateTime tokenValidity) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private void saveRefreshToken(long memberId, String refreshToken) {
        redisTemplate.opsForValue().set(
                String.valueOf(memberId),
                refreshToken,
                refreshTokenExpTime,
                TimeUnit.MILLISECONDS
        );
    }
}
