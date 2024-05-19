package uni.capstone.moodmingle.config.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.config.security.exception.advice.AuthExceptionLogger;
import uni.capstone.moodmingle.exception.BusinessException;
import uni.capstone.moodmingle.exception.code.ErrorCode;

import java.io.IOException;

import static uni.capstone.moodmingle.config.security.exception.code.AuthCode.*;

/**
 * 권한이 없을 경우 에러를 처리하는 핸들러
 *
 * @author ijin
 */
@Component
public class MingleAccessDeniedHandler implements AccessDeniedHandler {
    /**
     * ACCESS_DENIED 커스텀 예외 정보를 생성 -> 응답, 로깅
     *
     * @param request that resulted in an <code>AccessDeniedException</code>
     * @param response so that the user agent can be advised of the failure
     * @param accessDeniedException that caused the invocation
     * @throws IOException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        // 응답에 담을 JwtException 정보
        JSONObject exceptionInfo = createAccessDeniedResponseInfo();

        // 응답 & 로깅
        setAccessDeniedInfoToResponse(response, exceptionInfo);
        AuthExceptionLogger.logResponse(request, ACCESS_DENIED, exceptionInfo);
    }

    /**
     * HTTP Response 에 커스텀 ACCESS_DENIED 예외 정보 담기
     */
    private void setAccessDeniedInfoToResponse(HttpServletResponse response, JSONObject exceptionInfo) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(401);
        response.getWriter().print(exceptionInfo);
    }

    /**
     * AUTH Exception 발생 시, Response 의 Body 에 담기위한 Json 객체 생성
     *
     * @return JSONObject 객체
     */
    public JSONObject createAccessDeniedResponseInfo() {
        try {
            JSONObject responseJson = new JSONObject();
            responseJson.put("status", ACCESS_DENIED.getStatus().value());
            responseJson.put("error", ACCESS_DENIED.getStatus().name());
            responseJson.put("code", ACCESS_DENIED.getCode());
            responseJson.put("message", ACCESS_DENIED.getMessage());

            return responseJson;
        } catch (JSONException e) {
            throw new BusinessException(ErrorCode.SERVICE_UNAVAILABLE);
        }
    }
}
