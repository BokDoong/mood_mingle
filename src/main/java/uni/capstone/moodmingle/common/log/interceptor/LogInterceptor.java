package uni.capstone.moodmingle.common.log.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public interface LogInterceptor extends HandlerInterceptor {

    @Override
    void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                    @Nullable ModelAndView modelAndView);

    @Override
    boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler);
    
}
