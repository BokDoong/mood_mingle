package uni.capstone.moodmingle.config.security.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

/**
 * 커스텀 인증 예외정보를 담은 Enum 클래스
 *
 * @author ijin
 */
@Getter
@AllArgsConstructor
public enum AuthCode {

    /**
     * JWT 토큰 예외
     */
    EXPIRED_TOKEN(UNAUTHORIZED, "만료된 토큰", "AUTH-001"),
    INVALID_TOKEN(UNAUTHORIZED, "유효하지 않은 토큰", "AUTH-002"),
    UNSUPPORTED_TOKEN(FORBIDDEN, "지원하지 않는 토큰 형식", "AUTH-003"),
    BAD_REQUEST_TOKEN(UNAUTHORIZED, "토큰이 비어있거나 잘못된 타입일 경우", "AUTH-004"),

    /**
     * OIDC 예외
     */
    FAILED_RSA_ENCODING(NOT_IMPLEMENTED, "RSA 공개키 암호화 알고리즘이 실패한 경우", "AUTH-005"),

    /**
     * 사용자 인증 예외
     */
    ACCESS_DENIED(FORBIDDEN, "권한이 없음", "AUTH_006"),
    UNKNOWN_ERROR(SERVICE_UNAVAILABLE, "알수 없는 에러", "AUTH-007");

    /**
     * HTTP Status 와 각 Enum 에 담길 커스텀 예외 메세지와 에러코드
     */
    private final HttpStatus status;
    private final String message;
    private final String code;
}
