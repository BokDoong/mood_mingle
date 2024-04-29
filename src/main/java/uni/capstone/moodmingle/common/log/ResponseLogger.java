package uni.capstone.moodmingle.common.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@UtilityClass
public class ResponseLogger {

    // Logging Successful Response
    public void logSuccessfulResponse(HttpServletResponse response) {
        StringBuffer logBuffer = new StringBuffer();

        // Response's Representative Infos
        logBuffer.append(getLoggingStructure());
        logBuffer.append("[Response Status] : ").append(parseResponseStatus(response)).append("\n");
        logBuffer.append("[Response Headers] : ").append(parseResponseHeaders(response)).append("\n");

        // Response's Body
        logBuffer.append(parseResponseBody(response));

        // Logging
        log.info(logBuffer.toString());
    }

    // Parsing Response Status
    private String parseResponseStatus(HttpServletResponse response) {
        HttpStatus responseStatus = HttpStatus.valueOf(response.getStatus());
        return responseStatus.value() + " - " +
                responseStatus.getReasonPhrase();
    }

    // Parsing Response Headers
    private Map<String, Object> parseResponseHeaders(HttpServletResponse response) {
        Map<String, Object> headerMap = new HashMap<>();

        Collection<String> headerNames = response.getHeaderNames();
        for (String headerName : headerNames) {
            headerMap.put(headerName, response.getHeader(headerName));
        }
        return headerMap;
    }

    // Parsing Content of ResponseBody
    private String parseResponseBody(HttpServletResponse response) {
        final ContentCachingResponseWrapper cachingResponse = (ContentCachingResponseWrapper) response;

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

    // Logs' Title
    public String getLoggingStructure() {
        return """

                [Title] : Successful Responsing Information
                """;
    }
}
