package uni.capstone.moodmingle.common.log.advice;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Arrays;

/**
 * local, dev 프로파일에서 CachingBodyHttpServletWrapper, ContentCachingResponseWrapper 를 이용하여
 * RequestBody, ResponseBody 를 로깅하기 위해 캐싱을 설정하는 SpringFilter
 *
 * @author ijin
 */
@Profile("dev | local")
@Component
public class LogFilter extends OncePerRequestFilter {

    private static final String[] SWAGGER_REQUEST_URLS = {"/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**"};

    /**
     * Body 캐싱하여 요청은 다음 Filter 혹은 DispatcherServlet 에, 응답은 Client 에 전달
     *
     * @param request HTTP Request
     * @param response HTTP Response
     * @param filterChain HTTP FilterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Multipart Type, Swagger API 검사
        if (verifyMultipartFileIncluded(request) || verifySwaggerRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Request&Response Wrapping
        CachingBodyHttpServletWrapper wrappingRequest = new CachingBodyHttpServletWrapper(request);
        ContentCachingResponseWrapper wrappingResponse = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(wrappingRequest, wrappingResponse);

        // Client에 Wrapping된 Response 전달
        wrappingResponse.copyBodyToResponse();
    }

    /**
     * 파일을 포함하고 있는 지 검사
     *
     * @param request HTTP Request
     * @return 포함 여부
     */
    private boolean verifyMultipartFileIncluded(HttpServletRequest request) {
        if (request.getContentType() != null && request.getContentType().contains("multipart")) {
            request.setAttribute("isMultipartFile", true);
            return true;
        } else {
            request.setAttribute("isMultipartFile", false);
            return false;
        }
    }

    /**
     * Swagger 요청인지 검사
     *
     * @param request HTTP Request
     * @return 포함 여부
     */
    private boolean verifySwaggerRequest(HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        return Arrays.stream(SWAGGER_REQUEST_URLS).anyMatch(
                url -> url.equals(requestUrl)
        );
    }
}
