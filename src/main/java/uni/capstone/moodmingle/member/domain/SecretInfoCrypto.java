package uni.capstone.moodmingle.member.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.exception.BusinessException;
import uni.capstone.moodmingle.exception.code.ErrorCode;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 비밀키, 초기벡터 암호화 및 복호화 Crypto
 *
 * @author ijin
 */
@Component
public class SecretInfoCrypto {

    /**
     * 사용할 암호화 알고리즘과 마스터 비밀키, 초기벡터
     */
    private String encryptAlgorithm;
    private String masterKey;
    private String masterIv;

    public SecretInfoCrypto(
            @Value("${spring.security.algorithm}") String encryptAlgorithm,
            @Value("${spring.security.master-key}") String masterKey,
            @Value("${spring.security.master-iv}") String masterIv
    ) {
        this.encryptAlgorithm = encryptAlgorithm;
        this.masterKey = masterKey;
        this.masterIv = masterIv;
    }

    /**
     * 비밀키, 초기 벡터를 암호화
     *
     * @param secretInfo 비밀키 or 초기 벡터
     * @return           암호화된 데이터
     */
    public byte[] encryptSecretInfo(byte[] secretInfo){
        return encrypt(secretInfo);
    }

    /**
     * 비밀키 복호화
     *
     * @param encryptedKey  암호화된 비밀키
     * @return              비밀키
     * @throws Exception
     */
    public SecretKey decryptSecretKey(byte[] encryptedKey) {
        byte[] decryptedKey = decrypt(encryptedKey);
        return new SecretKeySpec(decryptedKey, encryptAlgorithm);
    }

    /**
     * 초기 벡터 복호화
     *
     * @param encryptedIv 암호화된 초기 벡터
     * @return            초기 벡터
     * @throws Exception
     */
    public IvParameterSpec decryptIV(byte[] encryptedIv) {
        byte[] decryptedIv = decrypt(encryptedIv);
        return new IvParameterSpec(decryptedIv);
    }

    private byte[] encrypt(byte[] value) {
        try {
            Cipher cipher = setCipher(Cipher.ENCRYPT_MODE);
            return cipher.doFinal(value);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.FAILED_ENCODING_DATA, e.getMessage());
        }
    }

    private byte[] decrypt(byte[] encryptedSecretInfo) {
        try {
            Cipher cipher = setCipher(Cipher.DECRYPT_MODE);
            return cipher.doFinal(encryptedSecretInfo);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.FAILED_DECODING_DATA, e.getMessage());
        }
    }

    private Cipher setCipher(int mode) throws Exception {
        Cipher cipher = Cipher.getInstance(encryptAlgorithm);
        cipher.init(mode, createMasterSecretKey(), createMasterIv());
        return cipher;
    }

    private SecretKeySpec createMasterSecretKey() {
        return new SecretKeySpec(masterKey.getBytes(), "AES");
    }

    private IvParameterSpec createMasterIv() {
        return new IvParameterSpec(masterIv.getBytes());
    }
}
