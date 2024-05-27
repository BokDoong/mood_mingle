package uni.capstone.moodmingle.diary.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.exception.BusinessException;
import uni.capstone.moodmingle.exception.code.ErrorCode;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 일기, 답장 암호화하는 인크립터
 *
 * @author ijin
 */
@Component
public class DiaryCrypto {

    /**
     * 사용할 암호화 알고리즘
     */
    private String encryptAlgorithm;

    public DiaryCrypto(
            @Value("${spring.security.algorithm}") String encryptAlgorithm
    ) {
        this.encryptAlgorithm = encryptAlgorithm;
    }

    /**
     * 암호화
     *
     * @param secretKey 비밀키
     * @param iv        초기 벡터
     * @param plainText 평문
     * @return          암호문
     */
    public String encrypt(SecretKey secretKey, IvParameterSpec iv, String plainText) {
        try {
            // Encoding Cipher 객체 생성
            Cipher cipher = Cipher.getInstance(encryptAlgorithm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            // 인코딩
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return new String(Base64.getEncoder().encode(encrypted));
        } catch (Exception exception) {
            throw new BusinessException(ErrorCode.FAILED_ENCODING_DIARY);
        }
    }

    /**
     * 복호호화
     *
     * @param secretKey  비밀키
     * @param iv         초기 벡터
     * @param cipherText 암호문
     * @return           평문
     */
    public String decrypt(SecretKey secretKey, IvParameterSpec iv, String cipherText) {
        try {
            // Decoding Cipher 객체 생성
            Cipher cipher = Cipher.getInstance(encryptAlgorithm);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            // 디코딩
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception exception) {
            throw new BusinessException(ErrorCode.FAILED_DECODING_DIARY);
        }
    }
}
