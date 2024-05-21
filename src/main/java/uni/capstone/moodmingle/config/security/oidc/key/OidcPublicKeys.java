package uni.capstone.moodmingle.config.security.oidc.key;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OidcPublicKeys {
    private List<PublicKeyInfo> keys;
}
