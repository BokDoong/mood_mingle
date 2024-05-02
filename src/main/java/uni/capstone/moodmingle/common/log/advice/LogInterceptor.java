package uni.capstone.moodmingle.common.log.advice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import uni.capstone.moodmingle.common.log.logger.RequestLogger;
import uni.capstone.moodmingle.common.log.logger.ResponseLogger;

/**
 * 캐싱된 Request, Response Body 를 Logger 에 전달
 *
 * @author ijin
 */
@Component
@RequiredArgsConstructor
public class LogInterceptor implements HandlerInterceptor {

    /**
     * Request, Response 를 로깅하는 Logger
     */
    private final RequestLogger requestLogger;
    private final ResponseLogger responseLogger;

    /**
     * Spring 관리 영역에 들어온 RequestBody 를 Logger 로 전달
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance evaluation
     * @return 다음 영역으로 진행하기 위해 항상 true 리턴
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        requestLogger.logRequest(request);
        return true;
    }

    /**
     * Spring Controller 에서 작업이 끝난 ResponseBody 를 Logger 로 전달
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler the handler (or {@link HandlerMethod}) that started asynchronous
     * execution, for type and/or instance examination
     * @param modelAndView the {@code ModelAndView} that the handler returned
     * (can also be {@code null})
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView){
        responseLogger.logResponse(response);
    }
}

