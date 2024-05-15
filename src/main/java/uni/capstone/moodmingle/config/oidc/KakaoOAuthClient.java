package uni.capstone.moodmingle.config.oidc;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import uni.capstone.moodmingle.config.oidc.dto.OIDCPublicKeys;

/**
 * 카카오로부터 공개키 받아오기 위해 통신하는 Kakao OAuth Client
 *
 * @author ijin
 */
@FeignClient(
        name = "KakaoOAuthClient",
        url = "https://kauth.kakao.com"
)
public interface KakaoOAuthClient {
    @Cacheable(cacheNames = "KakaoOIDC", cacheManager = "oidcCacheManager", key = "'KakaoOIDCPulbicKeys'")
    @GetMapping("/.well-known/jwks.json")
    OIDCPublicKeys getKakaoOIDCOpenKeys();
}
