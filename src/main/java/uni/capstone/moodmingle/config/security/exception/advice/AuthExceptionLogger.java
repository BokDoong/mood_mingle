package uni.capstone.moodmingle.config.security.exception.advice;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import uni.capstone.moodmingle.config.security.exception.code.AuthCode;

/**
 * Exception 발생했을 때, 커스텀 예외 정보를 담은 Logger
 *
 * @author ijin
 */
@Slf4j
@UtilityClass
public class AuthExceptionLogger {

    /**
     * 파싱 메서드를 이용하여 커스텀 AUTH 예외 정보를 담은 Response 를 로깅
     *
     * @param authExceptionInfo AUTH Exception 정보를 담은 Json Object
     * @param ex 예외 정보
     */
    public void logResponse(AuthCode ex, JSONObject authExceptionInfo) {
        StringBuffer logBuffer = new StringBuffer();

        // Response's Auth Exception Info
        logBuffer.append(getLoggingStructure());
        logBuffer.append("[AUTH Exception Class] : ").append(AuthCode.valueOf(ex.name())).append("\n");
        logBuffer.append("[Response Body With AUTH Exception] : ").append("\n").append(processAuthException(authExceptionInfo));

        // Logging & Flush
        log.warn(logBuffer.toString());
    }

    // Logs' Title
    public String getLoggingStructure() {
        return """

                [Title] : Handling Auth Exception Information
                """;
    }

    // Processing AuthException to Json Format
    private String processAuthException(JSONObject authExceptionInfo) {
        return authExceptionInfo
                .toString()
                .replace(",", ",\n    ")
                .replace("{", "{\n    ")
                .replace("}", "\n}");
    }
}
