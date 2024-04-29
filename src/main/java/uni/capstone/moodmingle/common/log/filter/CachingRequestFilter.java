package uni.capstone.moodmingle.common.log.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Profile("dev | local")
@Component
public class CachingRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Multipart Type 이면 Skip
        if (verifyMultipartFileIncluded(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Request&Response Wrapping
        ContentCachingRequestWrapper wrappingRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappingResponse = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(wrappingRequest, wrappingResponse);

        // Client에 Wrapping된 Response 전달
        wrappingResponse.copyBodyToResponse();
    }

    private boolean verifyMultipartFileIncluded(HttpServletRequest request) {
        if (request.getContentType() != null && request.getContentType().contains("multipart")) {
            request.setAttribute("isMultipartFile", true);
            return true;
        } else {
            request.setAttribute("isMultipartFile", false);
            return false;
        }
    }
}
