package uni.capstone.moodmingle.common.log.logger;

import jakarta.servlet.http.HttpServletRequest;

public interface RequestLogger {

    // Logging
    void logRequest(HttpServletRequest request);

    // Parsing Requested URI
    default String parseRequestURI(HttpServletRequest request) {
        String httpMethod = "[HTTP Method] : " + request.getMethod();
        String requestURI = "[Request URI] : " + request.getRequestURI();
        return httpMethod + "\n" + requestURI;
    }
}
