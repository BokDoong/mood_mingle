package uni.capstone.moodmingle.domain.member.exception;

import uni.capstone.moodmingle.global.error.ErrorCode;
import uni.capstone.moodmingle.global.error.exception.InvalidValueException;

public class MemberAlreadyExistException extends InvalidValueException {
    public MemberAlreadyExistException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MemberAlreadyExistException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
