package uni.capstone.moodmingle.exception.advice;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;
import uni.capstone.moodmingle.config.jwt.JwtException;
import uni.capstone.moodmingle.exception.ErrorResponse;

/**
 * Exception 발생했을 때, 커스텀 예외 정보를 담은 Logger
 *
 * @author ijin
 */
@Slf4j
@UtilityClass
public class ExceptionResponseLogger {

    /**
     * 파싱 메서드를 이용하여 커스텀 예외 정보를 담은 Response 를 로깅
     *
     * @param response ErrorResponse 를 담은 ResponseEntity
     * @param ex 예외 정보
     */
    public void logResponse(ResponseEntity<ErrorResponse> response, Exception ex) {
        StringBuffer logBuffer = new StringBuffer();

        // Response's Exception Info
        logBuffer.append(getLoggingStructure());
        logBuffer.append("[Exception Class] : ").append(parseExceptionName(ex)).append("\n");
        logBuffer.append("[Exception Message] : ").append(parseExceptionMessage(ex)).append("\n");
        logBuffer.append("[Response Body With Exception] : ").append("\n").append(parseResponseBody(response));

        // Logging & Flush
        log.warn(logBuffer.toString());
    }

    /**
     * 파싱 메서드를 이용하여 커스텀 JWT 예외 정보를 담은 Response 를 로깅
     *
     * @param jwtExceptionInfo JWT Exception 정보를 담은 Json Object
     * @param ex 예외 정보
     */
    public void logResponse(JwtException ex, JSONObject jwtExceptionInfo) {
        StringBuffer logBuffer = new StringBuffer();

        // Response's JWT Exception Info
        logBuffer.append(getLoggingStructure());
        logBuffer.append("[JWT Exception Class] : ").append(JwtException.valueOf(ex.name())).append("\n");
        logBuffer.append("[Response Body With JWT Exception] : ").append("\n").append(processJwtException(jwtExceptionInfo));

        // Logging & Flush
        log.warn(logBuffer.toString());
    }

    // Parsing Exception Class Name
    private String parseExceptionName(Exception e) {
        return e.getClass().getSimpleName();
    }

    // Parsing Exception Message
    private String parseExceptionMessage(Exception e) {
        return e.getMessage();
    }

    // Parsing Response Body
    private ErrorResponse parseResponseBody(ResponseEntity<ErrorResponse> response) {
        return response.getBody();
    }

    // Logs' Title
    public String getLoggingStructure() {
        return """

                [Title] : Handling Exception Information
                """;
    }

    // Processing JwtException with Json Format
    private String processJwtException(JSONObject jwtExceptionInfo) {
        return jwtExceptionInfo
                .toString()
                .replace(",", ",\n    ")
                .replace("{", "{\n    ")
                .replace("}", "\n}");
    }
}
