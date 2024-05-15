package uni.capstone.moodmingle.config.jwt.oidc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uni.capstone.moodmingle.config.oidc.KakaoOAuthClient;
import uni.capstone.moodmingle.config.oidc.dto.OIDCPublicKeys;

@Slf4j
@ExtendWith(MockitoExtension.class)
class KakaoOAuthClientTest {

    @Mock
    private KakaoOAuthClient kakaoOAuthClient;

    @Test
    public void 카카오_연동() throws Exception {
        OIDCPublicKeys oidcPublicKeys = kakaoOAuthClient.getKakaoOIDCOpenKeys();

        log.info("응기" + oidcPublicKeys.toString());
    }
}
