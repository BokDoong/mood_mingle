package uni.capstone.moodmingle.domain.diary.exception;

import uni.capstone.moodmingle.global.error.ErrorCode;
import uni.capstone.moodmingle.global.error.exception.CryptoException;

public class DiaryCryptoException extends CryptoException {
    public DiaryCryptoException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DiaryCryptoException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
