package uni.capstone.moodmingle.config.security.exception;

/**
 * 토큰에 대해서 검증이 실패했을 때 던져지는 커스텀 예외클래스
 *
 * @author ijin
 */
public class InvalidTokenException extends TokenException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
