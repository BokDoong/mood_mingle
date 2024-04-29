package uni.capstone.moodmingle.common.log.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import uni.capstone.moodmingle.common.log.RequestLogger;
import uni.capstone.moodmingle.common.log.ResponseLogger;

@Component
@Profile("dev | local")
@RequiredArgsConstructor
public class DevLoggingInterceptor implements LoggingInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @org.springframework.lang.Nullable ModelAndView modelAndView){
        logRequest(request);
        logSuccessfulResponse(response);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
    }

    private void logRequest(HttpServletRequest request) {
        RequestLogger.logDevRequest(request);
    }

    private void logSuccessfulResponse(HttpServletResponse response) {
        ResponseLogger.logSuccessfulResponse(response);
    }
}

