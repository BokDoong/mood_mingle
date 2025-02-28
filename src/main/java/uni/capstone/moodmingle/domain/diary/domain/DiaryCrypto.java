package uni.capstone.moodmingle.domain.diary.domain;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.domain.diary.exception.DiaryCryptoException;
import uni.capstone.moodmingle.global.error.ErrorCode;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * 일기, 답장 암호화하는 인크립터
 *
 * @author ijin
 */
@Component
public class DiaryCrypto {

    // 데이터, 비밀키 암호화 알고리즘
    private String encryptAlgorithm;
    private String keyEncryptAlgorithm;

    public DiaryCrypto(
            @Value("${spring.security.algorithm}") String encryptAlgorithm,
            @Value("${spring.security.key-algorithm}") String keyEncryptAlgorithm
    ) {
        this.encryptAlgorithm = encryptAlgorithm;
        this.keyEncryptAlgorithm = keyEncryptAlgorithm;
    }

    // 암호화
    public String encrypt(byte[] privateKey, String diary) {
        try {
            // 16 바이트의 초기벡터 랜덤 생성
            SecureRandom random = new SecureRandom();
            byte[] iv = new byte[16];
            random.nextBytes(iv);

            // 암호화시 사용할 키, 초기벡터, 알고리즘 설정
            SecretKeySpec secretKey = new SecretKeySpec(privateKey, keyEncryptAlgorithm);
            IvParameterSpec IV = new IvParameterSpec(iv);
            Cipher c = Cipher.getInstance(encryptAlgorithm);

            // 암호화
            c.init(Cipher.ENCRYPT_MODE, secretKey, IV);
            byte[] encryptionByte = c.doFinal(diary.getBytes("UTF-8"));

            // 결과 : 초기벡터 + 암호화 텍스트
            return Hex.encodeHexString(iv) + Hex.encodeHexString(encryptionByte);
        } catch (Exception e) {
            throw new DiaryCryptoException(ErrorCode.FAILED_ENCODING_DIARY);
        }
    }

    // 복호화
    public String decrypt(byte[] privateKey, String encryptedDiary) {
        try {
            // 데이터에서 초기벡터, 암호화된 텍스트 떼기
            byte[] iv = Hex.decodeHex(encryptedDiary.substring(0, 32).toCharArray());
            byte[] cipherText = Hex.decodeHex(encryptedDiary.substring(32).toCharArray());

            // 개인키, 초기벡터
            SecretKeySpec secretKey = new SecretKeySpec(privateKey, keyEncryptAlgorithm);
            IvParameterSpec IV = new IvParameterSpec(iv);

            // 복호화 알고리즘 및 환경설정
            Cipher c = Cipher.getInstance(encryptAlgorithm);
            c.init(Cipher.DECRYPT_MODE, secretKey, IV);

            // 결과
            return new String(c.doFinal(cipherText), "UTF-8");
        } catch (Exception e) {
            throw new DiaryCryptoException(ErrorCode.FAILED_DECODING_DIARY);
        }
    }
}
