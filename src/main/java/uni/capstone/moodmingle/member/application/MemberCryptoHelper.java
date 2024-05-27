package uni.capstone.moodmingle.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.member.application.dto.MemberCommandMapper;
import uni.capstone.moodmingle.member.domain.MemberSecretInfo;
import uni.capstone.moodmingle.member.domain.SecretInfoCrypto;
import uni.capstone.moodmingle.member.domain.SecretInfoFactory;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * 사용자 개인키, 초기 벡터 암호화 및 복호화 헬퍼 클래스
 *
 * @author ijin
 */
@Component
@RequiredArgsConstructor
public class MemberCryptoHelper {

    private final SecretInfoCrypto crypto;
    private final MemberCommandMapper mapper;
    private final SecretInfoFactory factory;

    /**
     * MemberSecretInfo 생성
     *
     * @param memberId  멤버 ID
     * @return          MemberSecretInfo
     */
    public MemberSecretInfo createSecretInfo(long memberId) {
        // 비밀키, IV 생성
        SecretKey secretKey = createSecretKey();
        IvParameterSpec iv = createIv();
        // 암호화
        return mapper.toSecretInfo(memberId, encryptSecretKey(secretKey), encryptIv(iv));
    }

    /**
     * 비밀키 복호화
     *
     * @param encryptedSecretKey 암호화된 비밀키
     * @return                   비밀키
     */
    public SecretKey decryptSecretKey(byte[] encryptedSecretKey) {
        return crypto.decryptSecretKey(encryptedSecretKey);
    }

    /**
     * 초기벡터 복호화
     *
     * @param encryptedIv 암호화된 초기벡터
     * @return            초기벡터
     */
    public IvParameterSpec decryptIv(byte[] encryptedIv) {
        return crypto.decryptIV(encryptedIv);
    }

    private IvParameterSpec createIv() {
        return factory.createIv();
    }

    private SecretKey createSecretKey() {
        return factory.createSecretKey();
    }

    private byte[] encryptSecretKey(SecretKey secretKey) {
        return crypto.encryptSecretInfo(secretKey.getEncoded());
    }

    private byte[] encryptIv(IvParameterSpec iv) {
        return crypto.encryptSecretInfo(iv.getIV());
    }
}
