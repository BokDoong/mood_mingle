package uni.capstone.moodmingle.config.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Redis 설정 Config 클래스
 *
 * @author ijin
 */
@Configuration
public class RedisConfig {

    /**
     * Redis 사용할 호스트&포트
     */
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.host}")
    private String host;

    /**
     * Redis 연결을 위한 LettuceConnectionFactory 설정
     *
     * @return
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    /**
     * 리프레쉬 토큰을 위해 사용할 RedisTemplate
     *
     * @return
     */
    @Bean
    @Qualifier("JwtTokenRedisTemplate")
    public RedisTemplate<String, String> redisTemplate() {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
}
