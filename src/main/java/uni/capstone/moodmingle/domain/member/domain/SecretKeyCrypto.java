package uni.capstone.moodmingle.domain.member.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 비밀키, 초기벡터 암호화 및 복호화 Crypto
 *
 * @author ijin
 */
@Component
public class SecretKeyCrypto {

    // 암호화 알고리즘, 서버 마스터키
    private String encryptAlgorithm;
    private String masterKey;

    public SecretKeyCrypto(
            @Value("${spring.security.algorithm}") String encryptAlgorithm,
            @Value("${spring.security.master-key}") String masterKey
    ) {
        this.encryptAlgorithm = encryptAlgorithm;
        this.masterKey = masterKey;
    }

    // 개인키 암호화

    // 개인키 복호화
}
