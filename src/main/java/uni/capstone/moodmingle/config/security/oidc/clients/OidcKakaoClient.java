package uni.capstone.moodmingle.config.security.oidc.clients;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import uni.capstone.moodmingle.config.security.oidc.key.OidcPublicKeys;

/**
 * 카카오로부터 공개키 받아오기 위해 통신하는 Kakao OAuth Client
 *
 * @author ijin
 */
@FeignClient(
        name = "KakaoOAuthClient",
        url = "https://kauth.kakao.com"
)
public interface OidcKakaoClient {
    @Cacheable(cacheNames = "KakaoOIDC", cacheManager = "oidcCacheManager", key = "'KakaoOIDCPulbicKeys'")
    @GetMapping("/.well-known/jwks.json")
    OidcPublicKeys getKakaoOIDCOpenKeys();
}
