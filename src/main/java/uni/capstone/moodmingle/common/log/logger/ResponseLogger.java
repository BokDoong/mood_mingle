package uni.capstone.moodmingle.common.log.logger;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

/**
 * ResponseBody 를 로깅하는 Logger 인터페이스
 *
 * @author ijin
 */
public interface ResponseLogger {

    /**
     * 로깅하는 메서드
     *
     * @param response HTTP Response
     */
    void logResponse(HttpServletResponse response);

    /**
     * 응답의 HTTP Status 를 파싱
     *
     * @param response HTTP Response
     * @return 파싱 결과
     */
    default String parseResponseStatus(HttpServletResponse response) {
        HttpStatus responseStatus = HttpStatus.valueOf(response.getStatus());
        return responseStatus.value() + " - " +
                responseStatus.getReasonPhrase();
    }
}
