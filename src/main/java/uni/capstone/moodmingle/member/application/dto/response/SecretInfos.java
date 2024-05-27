package uni.capstone.moodmingle.member.application.dto.response;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * 사용자 비밀키, 초기벡터 정보
 *
 * @param secretKey 비밀키
 * @param iv        초기 벡터
 */
public record SecretInfos(SecretKey secretKey, IvParameterSpec iv) {
}
