package uni.capstone.moodmingle.common.log;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@UtilityClass
public class RequestLogger {

    // Logging in Dev Profile
    public void logDevRequest(HttpServletRequest request) {
        StringBuffer logBuffer = new StringBuffer();

        // Request's Representative Infos
        logBuffer.append(getLoggingStructure());
        logBuffer.append(parseRequestURI(request)).append("\n");
        logBuffer.append("[Request Headers] : ").append(parseRequestHeaders(request)).append("\n");

        // Request Body
        if (!verifyMultipartFileContained(request)) {
            logBuffer.append("[Request Body] : ").append("\n").append(parseRequestBody(request));
        } else {
            logBuffer.append("[Request Body] : This request includes Multipart Files").append("\n");
        }

        // Logging & Flush
        log.info(logBuffer.toString());
    }

    // Logging in Prod Profile
    public void logProdRequest(HttpServletRequest request) {
        StringBuffer logBuffer = new StringBuffer();

        // Request's Representative Infos
        logBuffer.append(getLoggingStructure());
        logBuffer.append(parseRequestURI(request)).append("\n");

        // Logging & Flush
        log.info(logBuffer.toString());
    }

    // Check Multipart Files Included
    private boolean verifyMultipartFileContained(HttpServletRequest request) {
        return (boolean) request.getAttribute("isMultipartFile");
    }

    // Parsing Requested URI
    private String parseRequestURI(HttpServletRequest request) {
        String httpMethod = "[HTTP Method] : " + request.getMethod();
        String requestURI = "[Request URI] : " + request.getRequestURI();
        return httpMethod + "\n" + requestURI;
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
        final ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;

        if (request != null) {
            byte[] buf = cachingRequest.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    return new String(buf, 0, buf.length, cachingRequest.getCharacterEncoding());
                } catch (UnsupportedEncodingException e) {
                    return " Unsupported Encoding ";
                }
            }
        }
        return "EMPTY BODY ";
    }

    public String getLoggingStructure() {
        return """

                [Title] : Requested Information
                """;
    }
}
