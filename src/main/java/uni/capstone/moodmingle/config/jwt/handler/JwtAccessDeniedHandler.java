package uni.capstone.moodmingle.config.jwt.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.exception.advice.ExceptionResponseLogger;

import java.io.IOException;

import static uni.capstone.moodmingle.config.jwt.JwtException.*;

/**
 * 권한이 없을 경우 에러를 처리하는 핸들러
 *
 * @author ijin
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
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
        JSONObject jwtExceptionInfo = ACCESS_DENIED.createResponseInfo();

        // 응답 & 로깅
        setAccessDeniedInfoToResponse(response, jwtExceptionInfo);
        ExceptionResponseLogger.logResponse(ACCESS_DENIED, jwtExceptionInfo);
    }

    /**
     * HTTP Response 에 커스텀 ACCESS_DENIED 예외 정보 담기
     */
    private void setAccessDeniedInfoToResponse(HttpServletResponse response, JSONObject jwtExceptionInfo) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(401);
        response.getWriter().print(jwtExceptionInfo);
    }
}
