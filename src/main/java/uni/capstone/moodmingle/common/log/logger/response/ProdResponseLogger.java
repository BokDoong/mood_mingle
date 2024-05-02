package uni.capstone.moodmingle.common.log.logger.response;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.common.log.logger.ResponseLogger;

/**
 * prod 환경에서 Response 를 로깅하는 ResponseLogger 구현체
 *
 * @author ijin
 */
@Slf4j
@Component
@Profile("prod")
public class ProdResponseLogger implements ResponseLogger {

    /**
     * Response 를 다른 커스텀 파싱 메서드를 이용하여 로깅
     *
     * @param response HTTP Response
     */
    public void logResponse(HttpServletResponse response) {
        StringBuffer logBuffer = new StringBuffer();

        // Response's Representative Infos 파싱
        logBuffer.append("\n").append("[Title] : Successful Responsing Information").append("\n");
        logBuffer.append("[Response Status] : ").append(parseResponseStatus(response)).append("\n");

        log.info(logBuffer.toString());
    }
}
