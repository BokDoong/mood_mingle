package uni.capstone.moodmingle.common.log.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import uni.capstone.moodmingle.common.log.logger.RequestLogger;
import uni.capstone.moodmingle.common.log.logger.ResponseLogger;

@Component
@Profile("dev | local")
@RequiredArgsConstructor
public class DevLogInterceptor implements LogInterceptor {

    private final RequestLogger requestLogger;
    private final ResponseLogger responseLogger;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
    }

    // TODO: postHandle 이 아닌, preHandle 에서 Request 를 로깅할 수 있게 변경.
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView){
        requestLogger.logRequest(request);
        responseLogger.logResponse(response);
    }
}

