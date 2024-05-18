package uni.capstone.moodmingle.config.security.oidc.key;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PublicKeyInfo {
    private String kid;
    private String alg;
    private String use;
    private String n;
    private String e;
}
