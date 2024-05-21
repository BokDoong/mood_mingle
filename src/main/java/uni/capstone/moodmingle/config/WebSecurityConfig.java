package uni.capstone.moodmingle.config;

import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uni.capstone.moodmingle.config.security.handler.MingleAccessDeniedHandler;
import uni.capstone.moodmingle.config.security.handler.MingleAuthenticationEntryPoint;
import uni.capstone.moodmingle.config.security.jwt.JwtAuthFilter;
import uni.capstone.moodmingle.config.security.jwt.utils.JwtExtractor;
import uni.capstone.moodmingle.config.security.jwt.utils.JwtVerifier;
import uni.capstone.moodmingle.config.security.oidc.OidcAuthFilter;
import uni.capstone.moodmingle.config.security.oidc.key.KeyManager;
import uni.capstone.moodmingle.config.security.oidc.utils.OidcSignatureVerifier;
import uni.capstone.moodmingle.config.security.oidc.utils.OidcTokenExtractor;
import uni.capstone.moodmingle.config.security.oidc.utils.OidcTokenVerifier;

/**
 * Spring Security 설정하는 Config 클래스
 *
 * @author ijin
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private static final String[] AUTH_WHITELIST = {"/api/v1/member/reissue"};
    private static final String[] AUTH_BLACKLIST = {"/api/v1/diary", "/api/v1/diary/**", "/api/v1/member", "/api/v1/member/**"};

    /**
     * Beans for JWT, OIDC
     */
    private final JwtExtractor jwtExtractor;
    private final JwtVerifier jwtVerifier;
    private final OidcTokenVerifier oidcTokenVerifier;
    private final OidcTokenExtractor oidcTokenExtractor;
    private final KeyManager keyManager;
    private final OidcSignatureVerifier oidcSignatureVerifier;

    /**
     * 핸들러
     */
    private final MingleAuthenticationEntryPoint authenticationEntryPoint;
    private final MingleAccessDeniedHandler accessDeniedHandler;

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
                .requestMatchers(AUTH_WHITELIST).permitAll()
                .requestMatchers(AUTH_BLACKLIST).authenticated()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll());

        // Handler 설정
        http.exceptionHandling((exceptionHandling) -> exceptionHandling
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
        );

        // Filter 순서 설정
        http.addFilterBefore(new JwtAuthFilter(jwtExtractor, jwtVerifier), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new OidcAuthFilter(oidcTokenVerifier, oidcTokenExtractor, keyManager, oidcSignatureVerifier), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
