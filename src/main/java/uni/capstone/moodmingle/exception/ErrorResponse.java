package uni.capstone.moodmingle.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.jsonwebtoken.JwtException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import uni.capstone.moodmingle.exception.code.ErrorCode;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private final int status;
    private final String error;
    private final String code;
    private final String message;
    private final String detail;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.builder()
                        .status(errorCode.getStatus().value())
                        .error(errorCode.getStatus().name())
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode, String message) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.builder()
                        .status(errorCode.getStatus().value())
                        .error(errorCode.getStatus().name())
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .detail(message)
                        .build());
    }

    @Override
    public String toString() {
        return "{\n" +
                "    \"status\":" + status + ",\n" +
                "    \"error\":\"" + error + "\",\n" +
                "    \"code\":\"" + code + "\",\n" +
                "    \"message\":\"" + message + "\"\n" +
                "}";
    }
}
