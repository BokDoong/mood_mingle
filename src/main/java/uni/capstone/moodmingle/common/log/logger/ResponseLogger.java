package uni.capstone.moodmingle.common.log.logger;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

public interface ResponseLogger {

    // Logging
    void logResponse(HttpServletResponse response);

    // Parsing Response Status
    default String parseResponseStatus(HttpServletResponse response) {
        HttpStatus responseStatus = HttpStatus.valueOf(response.getStatus());
        return responseStatus.value() + " - " +
                responseStatus.getReasonPhrase();
    }
}
