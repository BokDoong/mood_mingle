package uni.capstone.moodmingle.config.security.oidc.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import uni.capstone.moodmingle.config.security.oidc.key.OidcPublicKeys;

@FeignClient(
        name = "AppleOIDCClient",
        url = "https://appleid.apple.com")
public interface OidcAppleClient {
    @GetMapping("/auth/keys")
    OidcPublicKeys getAppleOIDCOpenKeys();
}
