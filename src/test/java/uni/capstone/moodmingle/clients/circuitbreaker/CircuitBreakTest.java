package uni.capstone.moodmingle.clients.circuitbreaker;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.junit.jupiter.api.Test;
import uni.capstone.moodmingle.global.error.ErrorCode;
import uni.capstone.moodmingle.global.error.exception.ExternalApiException;

public class CircuitBreakTest {

    @Test
    public void 서킷브레이커_테스트() {

        for (int i = 0; i < 10; i++) {
            try {
                circuitBreakerMethods();
            } catch (Exception e) {
                System.out.println(i + "번째 요청 실패");
            }
        }
    }

    @CircuitBreaker(name = "openai-api", fallbackMethod = "circuitBreakerCallbackMethod")
    private void circuitBreakerMethods() {
        throw new ExternalApiException(ErrorCode.FAILED_IO_OPERATION);
    }

    // ✅ 서킷브레이커 오픈됐을 때 실행되는 fallback
    private void circuitBreakerCallbackMethod(Throwable error) {
        System.out.println("서킷 브레이커 OPEN 상태. 요청 차단됨.");
        throw new ExternalApiException(ErrorCode.CIRCUIT_BREAKER_OPENED);
    }
}
