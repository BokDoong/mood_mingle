package uni.capstone.moodmingle.exception;

import uni.capstone.moodmingle.exception.code.ErrorCode;

/**
 * BusinessException 를 상속하고, 비동기 작업을 실패했을 때 던져지는 커스텀 예외 클래스
 *
 * @author ijin
 */
public class AsyncTaskException extends BusinessException {
    /**
     * 기본 생성자
     *
     * @param errorCode 커스텀 ErrorCode
     */
    public AsyncTaskException(ErrorCode errorCode) {
        super(errorCode);
    }
}

