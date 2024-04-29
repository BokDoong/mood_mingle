package uni.capstone.moodmingle.common.log.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import uni.capstone.moodmingle.common.log.logger.RequestLogger;
import uni.capstone.moodmingle.common.log.logger.ResponseLogger;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class ProdLogInterceptor implements LogInterceptor {

    private final RequestLogger requestLogger;
    private final ResponseLogger responseLogger;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        requestLogger.logRequest(request);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        responseLogger.logResponse(response);
    }
}
