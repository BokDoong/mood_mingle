package uni.capstone.moodmingle.config.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import uni.capstone.moodmingle.exception.BusinessException;
import uni.capstone.moodmingle.exception.code.ErrorCode;

import static org.springframework.http.HttpStatus.*;

/**
 * 커스텀 JWT 예외정보를 담은 Enum 클래스
 *
 * @author ijin
 */
@Getter
@AllArgsConstructor
public enum JwtException {

    /**
     * 예외 정보
     */
    EXPIRED_TOKEN(UNAUTHORIZED, "만료된 토큰", "AUTH-001"),
    WRONG_TOKEN(UNAUTHORIZED, "유효하지 않은 토큰", "AUTH-002"),
    UNSUPPORTED_TOKEN(UNAUTHORIZED, "지원하지 않는 토큰 형식", "AUTH-003"),
    EMPTY_TOKEN(UNAUTHORIZED, "토큰이 없음", "AUTH-004"),
    ACCESS_DENIED(FORBIDDEN, "권한이 없음", "AUTH_005"),
    UNKNOWN_ERROR(SERVICE_UNAVAILABLE, "알수 없는 에러", "AUTH-006");

    /**
     * HTTP Status 와 각 Enum 에 담길 커스텀 예외 메세지와 에러코드
     */
    private final HttpStatus status;
    private final String message;
    private final String code;

    /**
     * JWT Exception 발생 시, Response 의 Body 에 담기위한 Json 객체 생성
     *
     * @return JSONObject 객체
     */
    public JSONObject createResponseInfo() {
        try {
            JSONObject responseJson = new JSONObject();
            responseJson.put("status", status.value());
            responseJson.put("error", status.name());
            responseJson.put("code", code);
            responseJson.put("message", message);

            return responseJson;
        } catch (JSONException e) {
            throw new BusinessException(ErrorCode.SERVICE_UNAVAILABLE);
        }
    }
}
