package uni.capstone.moodmingle.exception.advice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import uni.capstone.moodmingle.config.jwt.JwtException;
import uni.capstone.moodmingle.exception.ErrorResponse;

import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class ExceptionResponseLogger {

    // Logging Response With Exception
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

    // Logging Response With JWT-Exception
    public void logResponseWithJWTException(HttpServletRequest request, ResponseEntity<ErrorResponse> response, JwtException ex) {
        StringBuffer logBuffer = new StringBuffer();

        // Response's JWT Exception Info
        logBuffer.append(getLoggingStructure());
        logBuffer.append(parseRequestURI(request)).append("\n");
        logBuffer.append("[JWT Exception Class] : ").append(JwtException.valueOf(ex.name())).append("\n");
        logBuffer.append("[JWT Exception Message] : ").append(ex.getMessage()).append("\n");
        logBuffer.append("[Response Body With Exception] : ").append("\n").append(parseResponseBody(response));

        // Logging & Flush
        log.warn(logBuffer.toString());
    }

    // Parsing Exception Class Name
    private String parseExceptionName(Exception e) {
        return e.getClass().getSimpleName();
    }

    // Parsing Exception Message
    private String parseExceptionMessage(Exception e) {
        String message = e.getMessage();
        if (e.getClass().equals(MethodArgumentNotValidException.class)) {
            message = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(" and "));
        }
        if (message == null) {
            message = "EMPTY MESSAGE";
        }
        return message;
    }

    // Parsing Requested URI
    private String parseRequestURI(HttpServletRequest request) {
        String httpMethod = "[HTTP Method] : " + request.getMethod();
        String requestURI = "[Request URI] : " + request.getRequestURI();
        return httpMethod + "\n" + requestURI;
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
}
