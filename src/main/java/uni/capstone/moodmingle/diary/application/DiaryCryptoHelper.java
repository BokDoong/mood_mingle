package uni.capstone.moodmingle.diary.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.diary.domain.Diary;
import uni.capstone.moodmingle.diary.domain.DiaryCrypto;
import uni.capstone.moodmingle.member.application.dto.response.SecretInfos;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * 일기, 답장 암호화 헬 클래스
 *
 * @author ijin
 */
@Component
@RequiredArgsConstructor
public class DiaryCryptoHelper {

    private final DiaryCrypto crypto;

    /**
     * 일기, 답장 암호화
     *
     * @param secretInfos 사용자 비밀키, 초기 벡터
     * @param content   내용
     * @return          암호화된 데이터
     */
    public String encryptContent(SecretInfos secretInfos, String content) {
        return crypto.encrypt(secretInfos.secretKey(), secretInfos.iv(), content);
    }

    /**
     * 일기, 답장 복호화
     *
     * @param secretKey 사용자 비밀키
     * @param iv        사용자 초기 벡터
     * @param content   내용
     * @return          암호화된 데이터
     */
    public String decryptContent(SecretKey secretKey, IvParameterSpec iv, String content) {
        return crypto.decrypt(secretKey, iv, content);
    }
}
