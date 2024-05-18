package uni.capstone.moodmingle.config.security.exception;

/**
 * 만료된 토큰인 경우 던져지는 커스텀 예외클래스
 *
 * @author ijin
 */
public class ExpiredTokenException extends TokenException {
    public ExpiredTokenException(String message) {
        super(message);
    }
}
