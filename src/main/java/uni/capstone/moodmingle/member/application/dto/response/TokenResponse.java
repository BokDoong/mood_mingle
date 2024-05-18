package uni.capstone.moodmingle.member.application.dto.response;

import lombok.Builder;

/**
 * 액세스 토큰, 리프레쉬 토큰을 담은 DTO
 *
 * @param accessToken 액세스 토큰
 * @param refreshToken 리프레쉬 토큰
 */
@Builder
public record TokenResponse(String accessToken, String refreshToken) {
}
