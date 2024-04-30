package uni.capstone.moodmingle.common.log.logger.request;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.common.log.advice.CachingBodyHttpServletWrapper;
import uni.capstone.moodmingle.common.log.logger.RequestLogger;
import uni.capstone.moodmingle.exception.BusinessException;
import uni.capstone.moodmingle.exception.code.ErrorCode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Profile("dev | local")
public class DevRequestLogger implements RequestLogger {

    // Logging in Dev Profile
    @Override
    public void logRequest(HttpServletRequest request) {
        StringBuffer logBuffer = new StringBuffer();

        // Request's Representative Infos
        logBuffer.append("\n").append("[Title] : Requested Information").append("\n");
        logBuffer.append(parseRequestURI(request)).append("\n");
        logBuffer.append("[Request Headers] : ").append(parseRequestHeaders(request)).append("\n");

        // Request Body
        if (!verifyMultipartFileContained(request)) {
            logBuffer.append("[Request Body] : ").append("\n").append(parseRequestBody(request));
        } else {
            logBuffer.append("[Request Body] : This request includes Multipart Files").append("\n");
        }

        log.info(logBuffer.toString());
    }

    // Check Multipart Files Included
    private boolean verifyMultipartFileContained(HttpServletRequest request) {
        return (boolean) request.getAttribute("isMultipartFile");
    }

    // Parsing Requested Headers
    private Map<String, Object> parseRequestHeaders(HttpServletRequest request) {
        Map<String, Object> headerMap = new HashMap<>();

        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String headerName = headers.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap;
    }

    // Parsing Content of RequestBody
    private String parseRequestBody(HttpServletRequest request) {
        final CachingBodyHttpServletWrapper cachingRequest = (CachingBodyHttpServletWrapper) request;

        if (request != null) {
            try {
                ServletInputStream inputStream = cachingRequest.getInputStream();
                byte[] bodyBytes = new byte[1024];
                int bytesRead;
                StringBuilder body = new StringBuilder();

                while ((bytesRead = inputStream.read(bodyBytes)) != -1) {
                    body.append(new String(bodyBytes, 0, bytesRead, StandardCharsets.UTF_8));
                }
                return body.toString();
            } catch (IOException e) {
                throw new BusinessException(ErrorCode.DATA_IO_UNAVAILABLE);
            }
        }

        return "EMPTY BODY ";
    }
}
