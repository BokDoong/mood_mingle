package uni.capstone.moodmingle.config.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import uni.capstone.moodmingle.config.security.exception.ExpiredTokenException;
import uni.capstone.moodmingle.config.security.exception.InvalidTokenException;
import uni.capstone.moodmingle.config.security.exception.ParsingRequestedTokenException;
import uni.capstone.moodmingle.config.security.exception.code.AuthCode;
import uni.capstone.moodmingle.config.security.jwt.entity.JwtUserDetails;
import uni.capstone.moodmingle.config.security.jwt.utils.JwtExtractor;
import uni.capstone.moodmingle.config.security.jwt.utils.JwtVerifier;

import java.io.IOException;

/**
 * JWT 인증 필터
 *
 * @author ijin
 */
public class JwtAuthFilter extends OncePerRequestFilter {

    /**
     * JwtVerifier - 토큰 검증 Verifier
     * JwtExtractor - 토큰을 해독하는 Extractor
     */
    private final JwtExtractor jwtExtractor;
    private final JwtVerifier jwtVerifier;

    public JwtAuthFilter(JwtExtractor jwtExtractor, JwtVerifier jwtVerifier) {
        this.jwtExtractor = jwtExtractor;
        this.jwtVerifier = jwtVerifier;
    }

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
        // 로그인 요청이면 무시
        if (!checkUrlWhetherLoginOrJoin(request)) {
            try {
                // 헤더에서 토큰 추출하여 검증
                String token = extractTokenFromHeader(request);
                jwtVerifier.verifyTokenInfos(token);

                // 토큰 해독 -> Id 값으로 JwtUserDetails 생성
                Long userId = jwtExtractor.extractUserId(token);
                JwtUserDetails userDetails = new JwtUserDetails(userId);

                // Request 에 접근권한 설정 -> SpringSecurity 에 JwtUserDetails+인증정보 전달
                setAuthentication(userDetails);
            } catch (ParsingRequestedTokenException ex) {
                setJwtExceptionInfoToRequest(request, AuthCode.BAD_REQUEST_TOKEN);
            }  catch (InvalidTokenException ex) {
                setJwtExceptionInfoToRequest(request, AuthCode.INVALID_TOKEN);
            } catch (ExpiredTokenException ex) {
                setJwtExceptionInfoToRequest(request, AuthCode.EXPIRED_TOKEN);
            } catch (Exception ex) {
                setJwtExceptionInfoToRequest(request, AuthCode.UNKNOWN_ERROR);
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * HTTP Request 의 헤더에 담겨있는 토큰값 추출
     *
     * @param request HTTP Request
     * @return JWT 토큰
     */
    private String extractTokenFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        } else {
            throw new ParsingRequestedTokenException("Http 요청 Access Token 이 비어 있거나 Bearer 형식 토큰이 아닌 경우");
        }
    }

    /**
     * HTTP Request 에 예외 정보 추가
     *
     * @param request HTTP Request
     * @param exception 예외 정보
     */
    private void setJwtExceptionInfoToRequest(HttpServletRequest request, AuthCode exception) {
        request.setAttribute("exception", exception.name());
    }

    private void setAuthentication(JwtUserDetails userDetails) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    private boolean checkUrlWhetherLoginOrJoin(HttpServletRequest request) {
        return request.getRequestURI().equals("/api/v1/member/login") || request.getRequestURI().equals("/api/v1/member/join");
    }
}
