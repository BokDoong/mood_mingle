package uni.capstone.moodmingle.config.security.exception;

import io.jsonwebtoken.JwtException;

/**
 * 공개키 관련 커스텀 예외클래스
 *
 * @author ijin
 */
public class PublicKeyException extends JwtException {
    public PublicKeyException(String message) {
        super(message);
    }
}
