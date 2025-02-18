package uni.capstone.moodmingle.config.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Bucket4jTest {

    @Test
    public void 버킷_생성() {
        // 분당 토큰이 10개로 채워지는 버킷 생성
        Refill refill = Refill.intervally(10, Duration.ofMinutes(10));
        Bandwidth limit = Bandwidth.classic(10, refill);
        Bucket bucket = Bucket.builder()
                .addLimit(limit)
                .build();

        // 테스트
        for (int i = 1; i <= 10; i++) {
            Assertions.assertTrue(bucket.tryConsume(1));
        }
        Assertions.assertFalse(bucket.tryConsume(1));
    }

    @Test
    public void 초당_토큰생성되는_버킷() {
        // 초당 토큰이 2개씩 생성되는 버킷 생성
        Bandwidth limit = Bandwidth.classic(10, Refill.intervally(1, Duration.ofSeconds(2)));
        Bucket bucket = Bucket.builder().addLimit(limit).build();

        Assertions.assertTrue(bucket.tryConsume(1));     // first request
        Executors.newScheduledThreadPool(1)   // schedule another request for 2 seconds later
                .schedule(() -> Assertions.assertTrue(bucket.tryConsume(1)), 2, TimeUnit.SECONDS);

    }
}
