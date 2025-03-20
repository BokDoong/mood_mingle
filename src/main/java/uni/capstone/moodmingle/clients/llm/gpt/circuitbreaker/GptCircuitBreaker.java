package uni.capstone.moodmingle.clients.llm.gpt.circuitbreaker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GptCircuitBreaker {

    private final GptStatusTestClient gptStatusTestClient;
    private static CircuitBreakerStatus status = CircuitBreakerStatus.CLOSED;
    private int failureCount = 0;

    // 상태 조회
    public synchronized boolean checkCircuitBreakerOpened() {
        return status == CircuitBreakerStatus.OPEN;
    }

    // 실패 횟수 +1
    public synchronized void addFailureCount() {
        failureCount += 1;
        log.info("✅️실패 카운팅 : " + failureCount);
        if (failureCount > 10) {
            log.info("‼️서킷브레이커 오픈 ‼️");
            status = CircuitBreakerStatus.OPEN;
        }
    }

    // 10분마다 GPT API 상태 확인
    @Scheduled(fixedRate = 600000)
    public void testGptApiStatus() {
        if (status == CircuitBreakerStatus.OPEN) {
            if (gptStatusTestClient.requestToGptApi()) {        // 요청해서 200 응답이면 서킷브레이커 CLOSED 변환, 실패횟수 초기화
                status = CircuitBreakerStatus.CLOSED;
                failureCount = 0;
                log.info("‼️서킷브레이커 다시 닫힘 ‼️, 실패카운트 : " + failureCount);
            }
        }
    }
}

enum CircuitBreakerStatus {
    CLOSED,
    OPEN
}
