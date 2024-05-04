package uni.capstone.moodmingle.common.log.logger.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;
import uni.capstone.moodmingle.common.log.logger.ResponseLogger;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * local, dev 환경에서 Response 를 로깅하는 ResponseLogger 구현체
 *
 * @author ijin
 */
@Slf4j
@Component
@Profile("dev | local")
public class DevResponseLogger implements ResponseLogger {

    /**
     * Response 를 다른 커스텀 파싱 메서드를 이용하여 로깅
     *
     * @param response HTTP Response
     */
    public void logResponse(HttpServletResponse response) {
        StringBuffer logBuffer = new StringBuffer();

        // Response's Representative Infos 파싱
        logBuffer.append("\n").append("[Title] : Successful Responsing Information").append("\n");
        logBuffer.append("[Response Status] : ").append(parseResponseStatus(response)).append("\n");
        logBuffer.append("[Response Headers] : ").append(parseResponseHeaders(response)).append("\n");

        // Response's Body 파싱
        logBuffer.append(parseResponseBody(response));

        log.info(logBuffer.toString());
    }

    private Map<String, Object> parseResponseHeaders(HttpServletResponse response) {
        Map<String, Object> headerMap = new HashMap<>();

        Collection<String> headerNames = response.getHeaderNames();
        for (String headerName : headerNames) {
            headerMap.put(headerName, response.getHeader(headerName));
        }
        return headerMap;
    }

    private String parseResponseBody(HttpServletResponse response) {
        final ContentCachingResponseWrapper cachingResponse = new  ContentCachingResponseWrapper(response);

        if (cachingResponse != null) {
            byte[] buf = cachingResponse.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Object json = objectMapper.readValue(buf, Object.class);
                    return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
                } catch (IOException e) {
                    return "Failed to parse response body";
                }
            }
        }
        return "EMPTY BODY ";
    }
}
