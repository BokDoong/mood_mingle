package uni.capstone.moodmingle.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uni.capstone.moodmingle.config.jwt.JwtAuthFilter;
import uni.capstone.moodmingle.config.jwt.handler.JwtAccessDeniedHandler;
import uni.capstone.moodmingle.config.jwt.handler.JwtAuthenticationEntryPoint;
import uni.capstone.moodmingle.config.jwt.utils.JwtExtractor;
import uni.capstone.moodmingle.config.jwt.utils.JwtVerifier;

/**
 * Spring Security 설정하는 Config 클래스
 *
 * @author ijin
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

//    private static final String[] AUTH_BLACKLIST = {"/api/v1/diary", "/api/v1/diary/**", "/api/v1/member", "/api/v1/member/**"};
    private static final String[] AUTH_BLACKLIST = {};
    private final JwtExtractor jwtExtractor;
    private final JwtVerifier jwtVerifier;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;

    /**
     * Security 설정
     *
     * @param http HTTP
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CSRF 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        // STATELESS 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // FormLogin, BasicHttp 비활성화
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        // 권한 규칙 설정
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(AUTH_BLACKLIST).authenticated()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll());

        // JwtAuthFilter 추가
        http.addFilterBefore(new JwtAuthFilter(jwtExtractor, jwtVerifier), UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling((exceptionHandling) -> exceptionHandling
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
        );

        return http.build();
    }
}
