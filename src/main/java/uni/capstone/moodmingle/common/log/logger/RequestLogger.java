package uni.capstone.moodmingle.common.log.logger;

import jakarta.servlet.http.HttpServletRequest;

/**
 * RequestBody 를 로깅하는 Logger 인터페이스
 *
 * @author ijin
 */
public interface RequestLogger {

    /**
     * 로깅하는 메서드
     *
     * @param request HTTP Request
     */
    void logRequest(HttpServletRequest request);

    /**
     * 응답의 Request URI 를 파싱
     *
     * @param request HTTP Request
     * @return 파싱 결과
     */
    default String parseRequestURI(HttpServletRequest request) {
        String httpMethod = "[HTTP Method] : " + request.getMethod();
        String requestURI = "[Request URI] : " + request.getRequestURI();
        return httpMethod + "\n" + requestURI;
    }
}
