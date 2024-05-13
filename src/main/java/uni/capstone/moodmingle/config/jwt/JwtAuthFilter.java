package uni.capstone.moodmingle.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import uni.capstone.moodmingle.config.jwt.entity.JwtUserDetails;
import uni.capstone.moodmingle.config.jwt.utils.JwtExtractor;
import uni.capstone.moodmingle.config.jwt.utils.JwtVerifier;

import java.io.IOException;

/**
 * JWT 인증 필터
 *
 * @author ijin
 */
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    /**
     * JwtVerifier - 토큰 검증 Verifier
     * JwtExtractor - 토큰을 해독하는 Extractor
     */
    private final JwtExtractor jwtExtractor;
    private final JwtVerifier jwtVerifier;

    /**
     * Client 로부터 받은 토큰을 검증&해독 -> SpringSecurity 에 인증 정보 전달
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            // Jwt 유효성 검증
            if (jwtVerifier.validateToken(token, request)) {
                // 토큰 해독 -> Id 값으로 JwtUserDetails 생성
                Long userId = jwtExtractor.extractUserId(token);
                JwtUserDetails userDetails = new JwtUserDetails(userId);

                // Request 에 접근권한 설정 -> SpringSecurity 에 JwtUserDetails+인증정보 전달
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
