package uni.capstone.moodmingle.config.jwt.utils;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


/**
 * 토큰 만료시키는 클래스
 *
 * @author ijin
 */
@Component
public class JwtTokenManager {

    /**
     * RedisTemplate - 레디스에서 리프레쉬 토큰 정보를 갖고오기 위한 Template
     */
    private final RedisTemplate<String, String> redisTemplate;

    public JwtTokenManager(
            @Qualifier("JwtTokenRedisTemplate") RedisTemplate<String, String> redisTemplate
    ) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Redis 에서 리프레쉬 토큰 삭제
     *
     * @param userId 유저 ID
     */
    public void expireRefreshToken(long userId) {
        redisTemplate.delete(String.valueOf(userId));
    }
}
