package uni.capstone.moodmingle.config.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.config.security.exception.code.AuthCode;
import uni.capstone.moodmingle.config.security.exception.advice.AuthExceptionLogger;
import uni.capstone.moodmingle.exception.BusinessException;
import uni.capstone.moodmingle.exception.code.ErrorCode;

import java.io.IOException;

/**
 * 토큰 검증 및 인증이 실패했을 경우 핸들러
 *
 * @author ijin
 */
@Component
public class MingleAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Auth Exception 커스텀 예외 정보를 생성 -> 응답, 로깅
     *
     * @param request that resulted in an <code>AuthenticationException</code>
     * @param response so that the user agent can begin authentication
     * @param ex that caused the invocation
     * @throws IOException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException ex) throws IOException {
        // AuthException -> Json Message 변환
        AuthCode authCode = getAuthException(request);
        JSONObject exceptionInfo = createResponseInfo(authCode);

        // 응답에 AuthException 정보 담기 + 로깅
        setAuthExceptionInfoToResponse(response, exceptionInfo);
        AuthExceptionLogger.logResponse(request, authCode, exceptionInfo);
    }

    /**
     * Auth Exception 정보 추출
     */
    private AuthCode getAuthException(HttpServletRequest request) {
        String exception = (String) request.getAttribute("exception");
        return AuthCode.valueOf(exception);
    }

    /**
     * HTTP Response 에 커스텀 JWT 예외 정보 담기
     */
    private void setAuthExceptionInfoToResponse(HttpServletResponse response, JSONObject exceptionInfo) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(401);
        response.getWriter().print(exceptionInfo);
    }

    /**
     * AUTH Exception 발생 시, Response 의 Body 에 담기위한 Json 객체 생성
     *
     * @return JSONObject 객체
     */
    public JSONObject createResponseInfo(AuthCode exceptionInfo) {
        try {
            JSONObject responseJson = new JSONObject();
            responseJson.put("status", exceptionInfo.getStatus().value());
            responseJson.put("error", exceptionInfo.getStatus().name());
            responseJson.put("code", exceptionInfo.getCode());
            responseJson.put("message", exceptionInfo.getMessage());

            return responseJson;
        } catch (JSONException e) {
            throw new BusinessException(ErrorCode.SERVICE_UNAVAILABLE);
        }
    }
}
