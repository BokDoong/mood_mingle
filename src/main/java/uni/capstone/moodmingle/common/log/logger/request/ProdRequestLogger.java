package uni.capstone.moodmingle.common.log.logger.request;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.common.log.logger.RequestLogger;

/**
 * prod 환경에서 Request 를 로깅하는 RequestLogger 구현체
 *
 * @author ijin
 */
@Slf4j
@Component
@Profile("prod")
public class ProdRequestLogger implements RequestLogger {

    /**
     * Request 를 다른 커스텀 파싱 메서드를 이용하여 로깅
     *
     * @param request HTTP Request
     */
    @Override
    public void logRequest(HttpServletRequest request) {
        StringBuffer logBuffer = new StringBuffer();

        // Request's Representative Infos 파싱
        logBuffer.append("\n").append("[Title] : Requested Information").append("\n");
        logBuffer.append(parseRequestURI(request)).append("\n");

        log.info(logBuffer.toString());
    }
}
