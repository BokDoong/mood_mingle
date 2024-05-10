package uni.capstone.moodmingle.member.application.dto.response;

/**
 * 회원정보 조회 DTO Record
 *
 * @param name 이름
 * @param email 이메일
 */
public record MemberInfo(String name, String email) { }
