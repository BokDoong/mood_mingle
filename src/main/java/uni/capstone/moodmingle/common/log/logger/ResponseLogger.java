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
}
