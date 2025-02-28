package uni.capstone.moodmingle.clients.aws.kms;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.amazonaws.services.kms.model.EncryptionAlgorithmSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.global.error.ErrorCode;
import uni.capstone.moodmingle.global.error.exception.CryptoException;

import java.nio.ByteBuffer;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class KmsCrypto {

    @Value("${cloud.aws.kms.key}")
    private String key;

    private final AWSKMS kmsClient;
    private static final EncryptionAlgorithmSpec ALGORITHM = EncryptionAlgorithmSpec.SYMMETRIC_DEFAULT;

    // 암호화
    public String encrypt(byte[] privateKey) {
        try {
            // KMS 암호화 요청
            EncryptRequest encryptRequest = new EncryptRequest()
                    .withKeyId(key)
                    .withPlaintext(ByteBuffer.wrap(privateKey))
                    .withEncryptionAlgorithm(ALGORITHM);
            // 리턴
            byte[] encryptBytes = kmsClient.encrypt(encryptRequest).getCiphertextBlob().array();
            return Base64.getEncoder().encodeToString(encryptBytes);
        } catch (Exception e) {
            throw new CryptoException(ErrorCode.FAILED_ENCODE_DATA);
        }
    }

    // 복호화
    public byte[] decrypt(String encryptedKey) {
        try {
            // KMS 복호화 요청
            DecryptRequest decryptRequest = new DecryptRequest()
                    .withKeyId(key)
                    .withCiphertextBlob(ByteBuffer.wrap(Base64.getDecoder().decode(encryptedKey)))
                    .withEncryptionAlgorithm(ALGORITHM);
            // 리턴
            return kmsClient.decrypt(decryptRequest).getPlaintext().array();
        } catch (Exception e) {
            throw new CryptoException(ErrorCode.FAILED_DECODE_DATA);
        }
    }
}
