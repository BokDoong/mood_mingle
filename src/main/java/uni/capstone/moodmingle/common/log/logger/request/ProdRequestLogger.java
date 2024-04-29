package uni.capstone.moodmingle.common.log.logger.request;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.common.log.logger.RequestLogger;

@Slf4j
@Component
@Profile("prod")
public class ProdRequestLogger implements RequestLogger {

    // Logging in Prod Profile
    @Override
    public void logRequest(HttpServletRequest request) {
        StringBuffer logBuffer = new StringBuffer();

        // Request's Representative Infos
        logBuffer.append("\n\n").append("[Title] : Requested Information").append("\n");
        logBuffer.append(parseRequestURI(request)).append("\n");

        log.info(logBuffer.toString());
    }
}
