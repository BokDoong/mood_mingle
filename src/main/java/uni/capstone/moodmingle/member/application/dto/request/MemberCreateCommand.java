package uni.capstone.moodmingle.member.application.dto.request;

/**
 * 멤버 생성 Command Dto
 *
 * @param name 이름
 * @param email 이메일
 * @param imageUrl 사진 URL
 */
public record MemberCreateCommand(String name, String email, String imageUrl) {
}
