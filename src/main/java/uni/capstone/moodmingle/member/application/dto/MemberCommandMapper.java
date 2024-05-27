package uni.capstone.moodmingle.member.application.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import uni.capstone.moodmingle.member.application.dto.request.MemberCreateCommand;
import uni.capstone.moodmingle.member.domain.Member;
import uni.capstone.moodmingle.member.domain.MemberSecretInfo;

/**
 * Member 엔티티로 변환해주는 Mapper
 *
 * @author ijin
 */
@Mapper(componentModel = "spring")
public interface MemberCommandMapper {
    MemberCommandMapper INSTANCE = Mappers.getMapper(MemberCommandMapper.class);

    /**
     * MemberCreateCommand -> Member
     *
     * @param command Member 생성하기 위한 Command Dto
     * @return Member
     */
    Member toMember(MemberCreateCommand command);

    /**
     * Name, Email -> Member
     *
     * @param name 이름
     * @param email 이메일
     * @return Member
     */
    Member toMember(String name, String email);

    /**
     * 멤버 ID + 비밀키, 초기벡터 -> MemberSecretInfo
     *
     * @param memberId  멤버 ID
     * @param secretKey 비밀키
     * @param iv        초기 벡터
     * @return          MemberSecretInfo
     */
    MemberSecretInfo toSecretInfo(Long memberId, byte[] secretKey, byte[] iv);
}
