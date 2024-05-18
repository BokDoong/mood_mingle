package uni.capstone.moodmingle.config.security.oidc.key;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.config.security.exception.PublicKeyException;
import uni.capstone.moodmingle.config.security.oidc.clients.OidcKakaoClient;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

/**
 * 공개키를 관리하고 RSA 알고리즘으로 공개키를 조합하는 클래스
 */
@Component
@RequiredArgsConstructor
public class KeyManager {

    /**
     * 카카오 외부 서버와 연동하여 공개키 목록을 조회하는 Client
     */
    private final OidcKakaoClient kakaoClient;

    /**
     * 공개키 얻기
     *
     * @param kid Kid 값
     * @return 공개키
     */
    public Key getPublicKey(String kid) {
        try {
            // 카카오에게 공개키 요청 혹은 캐싱된 공개키 조회
            OidcPublicKeys oidcPublicKeys = kakaoClient.getKakaoOIDCOpenKeys();
            // kid 가 같은 키 조회
            PublicKeyInfo publicKeyInfo = findPublicKey(kid, oidcPublicKeys);
            // N,E -> 공개키 해시 알고리즘 적용
            return getRSAPublicKey(publicKeyInfo.getN(), publicKeyInfo.getE());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new PublicKeyException("RSA 공개키 암호화 알고리즘이 실패한 경우");
        }
    }

    /**
     * RSA Algorithm
     */
    private Key getRSAPublicKey(String modulus, String exponent)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodeN = Base64.getUrlDecoder().decode(modulus);
        byte[] decodeE = Base64.getUrlDecoder().decode(exponent);
        BigInteger n = new BigInteger(1, decodeN);
        BigInteger e = new BigInteger(1, decodeE);

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n, e);
        return keyFactory.generatePublic(keySpec);
    }

    private PublicKeyInfo findPublicKey(String kid, OidcPublicKeys oidcPublicKeys) {
        return oidcPublicKeys.getKeys().stream()
                .filter(o -> o.getKid().equals(kid))
                .findFirst()
                .orElseThrow();
    }
}
