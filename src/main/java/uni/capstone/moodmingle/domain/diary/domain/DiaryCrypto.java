package uni.capstone.moodmingle.domain.diary.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.domain.diary.exception.DiaryCryptoException;
import uni.capstone.moodmingle.global.error.ErrorCode;

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
            System.out.println(cipher.toString());
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);        // 문제 코드
            // 인코딩
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return new String(Base64.getEncoder().encode(encrypted));
        } catch (Exception exception) {
            throw new DiaryCryptoException(ErrorCode.FAILED_ENCODING_DIARY);
        }
    }

    /**
     * 복호화
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
            throw new DiaryCryptoException(ErrorCode.FAILED_DECODING_DIARY);
        }
    }
}
