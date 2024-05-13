package uni.capstone.moodmingle.config.jwt.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.config.jwt.JwtException;
import uni.capstone.moodmingle.exception.advice.ExceptionResponseLogger;

import java.io.IOException;

/**
 * 토큰 검증 및 인증이 실패했을 경우 핸들러
 *
 * @author ijin
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * JwtException 커스텀 예외 정보를 생성 -> 응답, 로깅
     *
     * @param request that resulted in an <code>AuthenticationException</code>
     * @param response so that the user agent can begin authentication
     * @param authException that caused the invocation
     * @throws IOException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // JwtException -> Json Message 변환
        JwtException jwtException = getJwtException(request);
        JSONObject jwtExceptionInfo = jwtException.createResponseInfo();

        // 응답에 JwtException 정보 담기 + 로깅
        setJwtExceptionInfoToResponse(response, jwtExceptionInfo);
        ExceptionResponseLogger.logResponse(jwtException, jwtExceptionInfo);
    }

    /**
     * JWT Exception 정보 추출
     */
    private JwtException getJwtException(HttpServletRequest request) {
        String exception = (String) request.getAttribute("exception");
        return exception == null ? JwtException.EMPTY_TOKEN : JwtException.valueOf(exception);
    }

    /**
     * HTTP Response 에 커스텀 JWT 예외 정보 담기
     */
    private void setJwtExceptionInfoToResponse(HttpServletResponse response, JSONObject jwtExceptionInfo) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(401);
        response.getWriter().print(jwtExceptionInfo);
    }
}
