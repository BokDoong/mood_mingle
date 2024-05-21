package uni.capstone.moodmingle.member.application.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import uni.capstone.moodmingle.member.application.dto.request.MemberCreateCommand;
import uni.capstone.moodmingle.member.domain.Member;

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
}
