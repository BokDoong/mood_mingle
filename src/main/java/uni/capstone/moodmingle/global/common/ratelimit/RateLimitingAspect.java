package uni.capstone.moodmingle.global.common.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uni.capstone.moodmingle.global.security.jwt.utils.JwtExtractor;
import uni.capstone.moodmingle.global.error.exception.BusinessException;
import uni.capstone.moodmingle.global.error.ErrorCode;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class RateLimitingAspect {

    private final Map<Long, Bucket> cache;      // userId 마다 버킷 저장할 해시맵
    private final JwtExtractor jwtExtractor;      // JWT 토큰 추출자

    public RateLimitingAspect(JwtExtractor jwtExtractor) {
        this.cache = new ConcurrentHashMap<>();
        this.jwtExtractor = jwtExtractor;
    }

    @Around("@annotation(uni.capstone.moodmingle.global.common.ratelimit.RateLimited)")
    public Object rateLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        // JWT 에서 userID 추출
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Long userId = jwtExtractor.extractUserId(request);

        // 사용자별 Rate Limiting 적용
        Bucket bucket = cache.computeIfAbsent(userId, k -> createNewBucket());

        // 토큰 소비
        if (bucket.tryConsume(1)) {
            return joinPoint.proceed();
        } else {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUESTS);
        }
    }

    // 10분에 4개의 토큰이 채워지는 버킷 생성
    private Bucket createNewBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1))))
                .build();
    }
}
