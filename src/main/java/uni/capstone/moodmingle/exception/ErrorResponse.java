package uni.capstone.moodmingle.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import uni.capstone.moodmingle.exception.code.ErrorCode;

/**
 * 최종적으로 예외 발생시, Client 가 받게되는 예외 정보
 *
 * @author ijin
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * status - ErrorCode 의 HTTP Status
     * error - ErrorCode 의 Enum 명
     * code - ErrorCode 의 커스텀 예외코드
     * message - ErrorCode 에 담긴 예외정보
     * detail - ErrorCode 와 함께 들어오는 메세지
     */
    private final int status;
    private final String error;
    private final String code;
    private final String message;
    private final String detail;

    /**
     * ErrorCode 를 가지고 ErrorResponse 를 생성하고 싶을 때 사용
     *
     * @param errorCode 커스텀 에러코드
     * @return ErrorResponse 를 담은 ResponseEntity
     */
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

    /**
     * 예외 메세지와 함께 ErrorResponse 를 생성하고 싶을 때 사용
     *
     * @param errorCode 커스텀 에러코드
     * @return ErrorResponse 를 담은 ResponseEntity
     */
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

    /**
     * HTTP Response 로 내려주기 위해 .toString()을 활용하여 String 객체로 변환
     *
     * @return String 객체로 변환된 값
     */
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
