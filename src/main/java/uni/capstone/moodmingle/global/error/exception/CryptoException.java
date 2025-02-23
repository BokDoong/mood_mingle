package uni.capstone.moodmingle.global.error.exception;

import uni.capstone.moodmingle.global.error.ErrorCode;

public class CryptoException extends BusinessException{
    public CryptoException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CryptoException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
