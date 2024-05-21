package uni.capstone.moodmingle.config.security.exception;

import io.jsonwebtoken.JwtException;

/**
 * 토큰 관련 커스텀 부모 예외클래스
 *
 * @author ijin
 */
public class TokenException extends JwtException {
    public TokenException(String message) {
        super(message);
    }
}
