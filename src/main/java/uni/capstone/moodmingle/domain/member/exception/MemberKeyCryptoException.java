package uni.capstone.moodmingle.domain.member.exception;

import uni.capstone.moodmingle.global.error.ErrorCode;
import uni.capstone.moodmingle.global.error.exception.CryptoException;

public class MemberKeyCryptoException extends CryptoException {
    public MemberKeyCryptoException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MemberKeyCryptoException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
