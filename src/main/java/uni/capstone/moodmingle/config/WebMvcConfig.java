package uni.capstone.moodmingle.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uni.capstone.moodmingle.common.log.advice.LogInterceptor;

/**
 * 커스텀 인터셉터를 설정하는 Config 클래스
 *
 * @author ijin
 */
@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LogInterceptor logInterceptor;

    /**
     * 로깅 인터셉터를 Spring 영역에 추가
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // Register Logging Interceptor
        registry.addInterceptor(logInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/vendor/**", "/css/*", "/img/*", "/error");
    }
}
