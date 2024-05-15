package uni.capstone.moodmingle.config.oidc.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OIDCPublicKeys {

    private List<PublicKey> keys;

    private record PublicKey(String kid, String kty, String aig,
                            String use, String n, String e) {
    }
}
