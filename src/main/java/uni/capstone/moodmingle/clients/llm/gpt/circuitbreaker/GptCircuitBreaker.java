package uni.capstone.moodmingle.clients.llm.gpt.circuitbreaker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GptCircuitBreaker {

    private final GptHealthCheckClient gptHealthCheckClient;
    private CircuitBreakerStatus status = CircuitBreakerStatus.CLOSED;
    private int failureCount = 0;
    private final Object lock = new Object();

    // 상태 조회
    public boolean checkCircuitBreakerOpened() {
        synchronized (lock) {
            return status == CircuitBreakerStatus.OPEN;
        }
    }

    // 실패 횟수 +1
    public void addFailureCount() {
        synchronized (lock) {
            failureCount += 1;
            log.info("✅️실패 카운팅 : " + failureCount);
            if (failureCount > 10) {
                log.info("‼️서킷브레이커 오픈 ‼️");
                status = CircuitBreakerStatus.OPEN;
            }
        }
    }

    // 10분마다 GPT API 상태 확인
    @Scheduled(fixedRate = 600000)
    public void testGptApiStatus() {
        // 서킷브레이커가 열려있을 때만 실행
        synchronized (lock) {
            if (!status.equals(CircuitBreakerStatus.OPEN)) {
                return;
            }
        }

        // 헬스 체킹
        boolean health = gptHealthCheckClient.requestToGptApi();
        if (health) {
            synchronized (lock) {
                status = CircuitBreakerStatus.CLOSED;
                failureCount = 0;
            }
            log.info("‼️서킷브레이커 다시 닫힘 ‼️, 실패카운트 : " + failureCount);
        }
    }

    enum CircuitBreakerStatus {
        CLOSED,
        OPEN
    }
}
