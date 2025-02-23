package uni.capstone.moodmingle.domain.diary.exception;

import uni.capstone.moodmingle.global.error.ErrorCode;
import uni.capstone.moodmingle.global.error.exception.EntityNotFoundException;

public class DiaryNotFoundException extends EntityNotFoundException {
    public DiaryNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DiaryNotFoundException(ErrorCode errorCode, long id) {
        super(errorCode, id);
    }
}
