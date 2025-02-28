package uni.capstone.moodmingle.global.error.exception;

import uni.capstone.moodmingle.global.error.ErrorCode;

public class ExternalApiException extends BusinessException {
    public ExternalApiException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ExternalApiException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
