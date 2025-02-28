package uni.capstone.moodmingle.clients.kms;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.amazonaws.services.kms.model.EncryptionAlgorithmSpec;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uni.capstone.moodmingle.global.error.ErrorCode;
import uni.capstone.moodmingle.global.error.exception.CryptoException;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

class KmsCryptoTest {

    private String accessKey = "";
    private String secretKey = "";
    private String kmsKey = "";

    private static final EncryptionAlgorithmSpec ALGORITHM = EncryptionAlgorithmSpec.SYMMETRIC_DEFAULT;

    @Test
    @DisplayName("암복호화 테스트")
    public void cryptoTest() {
        // given
        final String plainText = "Hello, World!";
        String encryptText = "";
        String decryptText = "";

        // when
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AWSStaticCredentialsProvider awsStaticCredentialsProvider = new AWSStaticCredentialsProvider(credentials);
        AWSKMS kmsClient = AWSKMSClientBuilder.standard()
                .withCredentials(awsStaticCredentialsProvider)
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();

        // 암호화
        try {
            // 암호화
            EncryptRequest encryptRequest = new EncryptRequest();
            encryptRequest.withKeyId(kmsKey);
            encryptRequest.withPlaintext(ByteBuffer.wrap(plainText.getBytes(StandardCharsets.UTF_8)));
            encryptRequest.withEncryptionAlgorithm(ALGORITHM);

            // 리턴
            byte[] encryptBytes = kmsClient.encrypt(encryptRequest).getCiphertextBlob().array();
            encryptText = Base64.getEncoder().encodeToString(encryptBytes);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new CryptoException(ErrorCode.FAILED_ENCODE_DATA);
        }

        // 복호화
        try {
            // 복호화
            DecryptRequest decryptRequest = new DecryptRequest();
            decryptRequest.withKeyId(kmsKey);
            decryptRequest.withCiphertextBlob(ByteBuffer.wrap(Base64.getDecoder().decode(encryptText)));
            decryptRequest.withEncryptionAlgorithm(ALGORITHM);

            byte[] plainTextBytes = kmsClient.decrypt(decryptRequest).getPlaintext().array();
            decryptText = new String(plainTextBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new CryptoException(ErrorCode.FAILED_DECODE_DATA);
        }

        System.out.println("PlainText: " + plainText);
        System.out.println("EncryptText: " + encryptText);
        System.out.println("DecryptText: " + decryptText);
    }

}
