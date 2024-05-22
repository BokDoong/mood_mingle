package uni.capstone.moodmingle.diary.domain;

import lombok.experimental.UtilityClass;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.exception.BusinessException;
import uni.capstone.moodmingle.exception.code.ErrorCode;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 일기, 답장 암호화하는 인크립터
 *
 * @author ijin
 */
@Component
public class DiaryReplyCrypto implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 서버가 시작되면, 생성될 시크릿키&초기 벡터
     */
    public static SecretKey aesKey;
    public static IvParameterSpec aesIv;

    /**
     * 암호화 알고리즘
     */
    private static final String SPEC = "AES/CBC/PKCS5Padding";
    /**
     * 암호화
     */
    public static String encrypt(String plainText) {
        try {
            // Encoding Cipher 객체 생성
            Cipher cipher = Cipher.getInstance(SPEC);
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, aesIv);
            // 인코딩
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return new String(Base64.getEncoder().encode(encrypted));
        } catch (Exception exception) {
            throw new BusinessException(ErrorCode.FAILED_ENCODING_DIARY);
        }
    }

    /**
     * 복호화
     */
    public static String decrypt(String cipherText) {
        try {
            // Decoding Cipher 객체 생성
            Cipher cipher = Cipher.getInstance(SPEC);
            cipher.init(Cipher.DECRYPT_MODE, aesKey, aesIv);
            // 디코딩
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception exception) {
            throw new BusinessException(ErrorCode.FAILED_DECODING_DIARY);
        }
    }

    /**
     * 서버가 실행되면 aesKey 안에 key 값이 들어가며 초기화
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            // AES SecretKey 생성
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            aesKey = keyGenerator.generateKey();

            // IV 생성
            byte[] iv = new byte[16];
            new SecureRandom().nextBytes(iv);
            aesIv =  new IvParameterSpec(iv);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("AES 알고리즘 키 생성 실패", e);
        }
    }
}
