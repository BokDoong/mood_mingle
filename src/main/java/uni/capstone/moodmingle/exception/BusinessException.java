package uni.capstone.moodmingle.exception;

import lombok.Getter;
import uni.capstone.moodmingle.exception.code.ErrorCode;

/**
 * RuntimeException 을 상속하여 커스텀 ErrorCode 를 포함하여 던져지는 커스텀 예외 클래스
 *
 * @author ijin
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 커스텀 ErrorCode
     */
    private final ErrorCode errorCode;

    /**
     * 기본 생성자
     *
     * @param errorCode 커스텀 ErrorCode
     */
    public BusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * ErrorCode 와 함께 예외메세지를 던지고 싶을 때, 사용되는 생성자
     *
     * @param errorCode 커스텀 ErrorCode
     * @param message 예외 메세지
     */
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
