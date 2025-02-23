package uni.capstone.moodmingle.global.security.oidc.key;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OidcPublicKeys {
    private List<PublicKeyInfo> keys;
}
