package uni.capstone.moodmingle.common.log.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import uni.capstone.moodmingle.common.log.RequestLogger;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class ProdLoggingInterceptor implements LoggingInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        RequestLogger.logProdRequest(request);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }
}
