package uni.capstone.moodmingle.config.security.oidc;

import io.jsonwebtoken.Claims;
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
import uni.capstone.moodmingle.config.security.exception.PublicKeyException;
import uni.capstone.moodmingle.config.security.exception.code.AuthCode;
import uni.capstone.moodmingle.config.security.oidc.entity.OidcUserInfo;
import uni.capstone.moodmingle.config.security.oidc.key.KeyManager;
import uni.capstone.moodmingle.config.security.oidc.utils.OidcSignatureVerifier;
import uni.capstone.moodmingle.config.security.oidc.utils.OidcTokenExtractor;
import uni.capstone.moodmingle.config.security.oidc.utils.OidcTokenVerifier;

import java.io.IOException;
import java.security.Key;

/**
 * OIDC 인증 필터
 *
 * @author ijin
 */
public class OidcAuthFilter extends OncePerRequestFilter {

    /**
     * OidcTokenManager - 토큰 검증 및 클레임 생성
     */
    private final OidcTokenVerifier tokenVerifier;
    private final OidcTokenExtractor oidcTokenExtractor;
    private final KeyManager keyManager;
    private final OidcSignatureVerifier signatureVerifier;

    public OidcAuthFilter(OidcTokenVerifier tokenVerifier, OidcTokenExtractor oidcTokenExtractor,
                          KeyManager keyManager, OidcSignatureVerifier signatureVerifier) {
        this.tokenVerifier = tokenVerifier;
        this.oidcTokenExtractor = oidcTokenExtractor;
        this.keyManager = keyManager;
        this.signatureVerifier = signatureVerifier;
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
        if (checkUrlWhetherLoginOrJoin(request)) {
            try {
                // 토큰 추출 및 페이로드 검증
                String token = extractTokenFromHeader(request);
                String authServer = getAuthServer(request);

                // 토큰 검증
                tokenVerifier.verifyTokenInfos(authServer, token);

                // Kid 값 추출 -> DB 에서 서명 검증에 사용할 공개키 찾는다
                String kid = oidcTokenExtractor.extractKidValue(token);
                Key publicKey = keyManager.getPublicKey(authServer, kid);
                // 공개키로 Body 추출
                Claims body = signatureVerifier.verifyAndGetOidcTokenJws(token, publicKey).getBody();

                // UserInfo 추출 -> Security 인증
                OidcUserInfo oidcUserInfo = oidcTokenExtractor.extractUserInfo(authServer, body);
                setAuthentication(oidcUserInfo);
            } catch (ParsingRequestedTokenException ex) {
                setOidcExceptionInfoToRequest(request, AuthCode.BAD_REQUEST_TOKEN);
            } catch (InvalidTokenException ex) {
                setOidcExceptionInfoToRequest(request, AuthCode.INVALID_TOKEN);
            } catch (ExpiredTokenException ex) {
                setOidcExceptionInfoToRequest(request, AuthCode.EXPIRED_TOKEN);
            } catch (PublicKeyException ex) {
                setOidcExceptionInfoToRequest(request, AuthCode.FAILED_RSA_ENCODING);
            } catch (Exception ex) {
                setOidcExceptionInfoToRequest(request, AuthCode.UNKNOWN_ERROR);
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * HTTP Request 의 헤더에 담겨있는 토큰값 추출
     *
     * @param request HTTP Request
     * @return 토큰
     */
    private String extractTokenFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        } else {
            throw new ParsingRequestedTokenException("Http 요청 ID_Token 이 비어 있거나 Bearer 형식 토큰이 아닌 경우");
        }
    }

    /**
     * HTTP Request 에 예외 정보 추가
     *
     * @param request HTTP Request
     * @param exception 예외 정보
     */
    private void setOidcExceptionInfoToRequest(HttpServletRequest request, AuthCode exception) {
        request.setAttribute("exception", exception.name());
    }
    
    private void setAuthentication(OidcUserInfo oidcUserInfo) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(oidcUserInfo, null, null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    private boolean checkUrlWhetherLoginOrJoin(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/v1/member/login/") || request.getRequestURI().startsWith("/api/v1/member/join/");
    }

    private String getAuthServer(HttpServletRequest request) {
        return request.getRequestURI().split("/")[5];
    }
}
