package uni.capstone.moodmingle.common.log.logger.response;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.common.log.logger.ResponseLogger;

@Slf4j
@Component
@Profile("prod")
public class ProdResponseLogger implements ResponseLogger {

    // Logging in Prod Profile
    public void logResponse(HttpServletResponse response) {
        StringBuffer logBuffer = new StringBuffer();

        // Response's Representative Infos
        logBuffer.append("\n").append("[Title] : Successful Responsing Information").append("\n");
        logBuffer.append("[Response Status] : ").append(parseResponseStatus(response)).append("\n");

        log.info(logBuffer.toString());
    }
}
