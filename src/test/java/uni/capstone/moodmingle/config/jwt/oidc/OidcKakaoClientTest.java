package uni.capstone.moodmingle.config.jwt.oidc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uni.capstone.moodmingle.config.security.oidc.clients.OidcKakaoClient;
import uni.capstone.moodmingle.config.security.oidc.key.OidcPublicKeys;

@Slf4j
@ExtendWith(MockitoExtension.class)
class OidcKakaoClientTest {

    @Mock
    private OidcKakaoClient oidcKakaoClient;

    @Test
    public void 카카오_연동() throws Exception {
        OidcPublicKeys oidcPublicKeys = oidcKakaoClient.getKakaoOIDCOpenKeys();

        log.info("응기" + oidcPublicKeys.toString());
    }
}
