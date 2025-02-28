package uni.capstone.moodmingle.config.jwt.oidc;

import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;
import uni.capstone.moodmingle.domain.diary.exception.DiaryCryptoException;
import uni.capstone.moodmingle.global.error.ErrorCode;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionTest {
    @Test
    public void 암호화_복호화_테스트() throws Exception {
        // 평문
        String plainText = "하이용.";
        String privateKey = generateUserPrivateKey();

        // 암호화 : 초기벡터+암호화문
        String encryptedText = encrypt(privateKey, plainText);
        System.out.println(encryptedText);

        // 복호화
        String decryptedText = decrypt(privateKey, encryptedText);
        System.out.println(decryptedText);
    }

    private String decrypt(String privateKey, String encryptedText) {
        try {
            // Base64로 인코딩된 개인키를 디코딩하여 바이트 배열로 변환
            byte[] decodedPrivateKey = Base64.getDecoder().decode(privateKey);
            // 데이터에서 초기벡터, 암호화된 텍스트 떼기
            byte[] iv = Hex.decodeHex(encryptedText.substring(0, 32).toCharArray());
            byte[] cipherText = Hex.decodeHex(encryptedText.substring(32).toCharArray());

            // 개인키, 초기벡터
            SecretKeySpec secretKey = new SecretKeySpec(decodedPrivateKey, "AES");
            IvParameterSpec IV = new IvParameterSpec(iv);

            // 복호화 알고리즘 및 환경설정
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, secretKey, IV);

            // 결과
            return new String(c.doFinal(cipherText), "UTF-8");
        } catch (Exception e) {
            throw new DiaryCryptoException(ErrorCode.FAILED_DECODING_DIARY);
        }
    }

    private String encrypt(String privateKey, String plainText) {
        try {
            // Base64로 인코딩된 개인키를 디코딩하여 바이트 배열로 변환
            byte[] decodedPrivateKey = Base64.getDecoder().decode(privateKey);

            // 16 바이트의 초기벡터 랜덤 생성
            SecureRandom random = new SecureRandom();
            byte[] iv = new byte[16];
            random.nextBytes(iv);

            // 암호화시 사용할 키, 초기벡터, 알고리즘 설정
            SecretKeySpec secretKey = new SecretKeySpec(decodedPrivateKey, "AES");
            IvParameterSpec IV = new IvParameterSpec(iv);
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");

            // 암호화
            c.init(Cipher.ENCRYPT_MODE, secretKey, IV);
            byte[] encryptionByte = c.doFinal(plainText.getBytes("UTF-8"));

            // 결과 : 초기벡터 + 암호화 텍스트
            return Hex.encodeHexString(iv) + Hex.encodeHexString(encryptionByte);
        } catch (Exception e) {
            throw new DiaryCryptoException(ErrorCode.FAILED_ENCODING_DIARY);
        }
    }

    private String generateUserPrivateKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[32];
        secureRandom.nextBytes(keyBytes); // 안전한 랜덤 키 생성

        return Base64.getEncoder().encodeToString(keyBytes); // Base64 인코딩하여 저장 가능하도록 변환
    }
}
