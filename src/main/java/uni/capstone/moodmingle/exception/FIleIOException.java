package uni.capstone.moodmingle.exception;

import uni.capstone.moodmingle.exception.code.ErrorCode;

/**
 * BusinessException 를 상속하고, File I/O 과정에서 예외가 발생했을 때 던져지는 커스텀 예외 클래스
 * 예외 정보의 가독성을 높이며 코드의 유지보수성을 향상하기 위함.
 *
 * @author ijin
 */
public class FIleIOException extends BusinessException {
    /**
     * 기본 생성자
     *
     * @param errorCode 커스텀 ErrorCode
     */
    public FIleIOException(ErrorCode errorCode) {
        super(errorCode);
    }
}

