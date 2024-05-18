package uni.capstone.moodmingle.config.security.exception;

/**
 * 요청된 HTTP 토큰의 파싱 실패했을 때 던져지는 커스텀 에러
 *
 * @author ijin
 */
public class ParsingRequestedTokenException extends TokenException {
    public ParsingRequestedTokenException(String message) {
        super(message);
    }
}
