package uni.capstone.moodmingle.domain.diary.exception;

import uni.capstone.moodmingle.global.error.ErrorCode;
import uni.capstone.moodmingle.global.error.exception.InvalidValueException;

public class DiaryAlreadyExistException extends InvalidValueException {
    public DiaryAlreadyExistException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DiaryAlreadyExistException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
