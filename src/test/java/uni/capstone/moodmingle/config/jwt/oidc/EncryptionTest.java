package uni.capstone.moodmingle.config.jwt.oidc;

import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;
import uni.capstone.moodmingle.domain.diary.exception.DiaryCryptoException;
import uni.capstone.moodmingle.global.error.ErrorCode;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class EncryptionTest {

    // 개인키
    private String privateKey = "AES_PRIVATE_KEY_THIS_TEST_32BYTE";

    @Test
    public void 암호화_복호화_테스트() throws Exception {
        // 평문
        String plainText = "하이용.";

        // 암호화 : 초기벡터+암호화문
        String encryptedText = encrypt(plainText);
        System.out.println(encryptedText);

        // 복호화
        String decryptedText = decrypt(encryptedText);
        System.out.println(decryptedText);
    }

    private String decrypt(String encryptedText) {
        try {
            byte[] iv = Hex.decodeHex(encryptedText.substring(0, 32).toCharArray());
            byte[] cipherText = Hex.decodeHex(encryptedText.substring(32).toCharArray());

            // 개인키, 초기벡터
            SecretKeySpec secretKey = new SecretKeySpec(privateKey.getBytes("UTF-8"), "AES");
            IvParameterSpec IV = new IvParameterSpec(iv);

            // 복호화 알고리즘 및 환경설정
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, secretKey, IV);

            // 결과
            String decryptedText = new String(c.doFinal(cipherText), "UTF-8");
            return decryptedText;
        } catch (Exception e) {
            throw new DiaryCryptoException(ErrorCode.FAILED_DECODING_DIARY);
        }
    }

    private String encrypt(String plainText) {
        try {
            // 16 바이트의 초기벡터 랜덤 생성
            SecureRandom random = new SecureRandom();
            byte[] iv = new byte[16];
            random.nextBytes(iv);

            // 암호화시 사용할 키, 초기벡터, 알고리즘 설정
            SecretKeySpec secretKey = new SecretKeySpec(privateKey.getBytes("UTF-8"), "AES");
            IvParameterSpec IV = new IvParameterSpec(iv);
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");

            // 암호화
            c.init(Cipher.ENCRYPT_MODE, secretKey, IV);
            byte[] encryptionByte = c.doFinal(plainText.getBytes("UTF-8"));
            // 결과 : 초기벡터+암호화 텍스트
            String encryptedResult = Hex.encodeHexString(iv) + Hex.encodeHexString(encryptionByte);
            return encryptedResult;
        } catch (Exception e) {
            throw new DiaryCryptoException(ErrorCode.FAILED_ENCODING_DIARY);
        }
    }
}
