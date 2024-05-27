package uni.capstone.moodmingle.member.domain;

import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.exception.BusinessException;
import uni.capstone.moodmingle.exception.code.ErrorCode;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 비밀키, 초기 벡터 생성하는 팩토리
 *
 * @author ijin
 */
@Component
public class SecretInfoFactory {

    /**
     * SecretKey 생성
     */
    public SecretKey createSecretKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessException(ErrorCode.FAILED_MAKING_SECRET_KEY);
        }
    }

    /**
     * 초기 벡터(IV) 생성
     */
    public IvParameterSpec createIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
}
