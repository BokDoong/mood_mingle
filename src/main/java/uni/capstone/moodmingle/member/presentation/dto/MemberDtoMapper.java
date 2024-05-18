package uni.capstone.moodmingle.member.presentation.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import uni.capstone.moodmingle.config.security.oidc.entity.OidcUserInfo;
import uni.capstone.moodmingle.member.application.dto.request.MemberCreateCommand;

/**
 * Client 로부터 받은 Presentation 계층의 DTO -> Application 계층의 DTO 로 바꿔주는 Mapper
 *
 * @author ijin
 */
@Mapper(componentModel = "spring")
public interface MemberDtoMapper {
    MemberDtoMapper INSTANCE = Mappers.getMapper(MemberDtoMapper.class);

    /**
     * OidcUserInfo -> MemberCreateCommand 로 매핑
     *
     * @param oidcUserInfo OIDC 인증된 사용자 정보
     * @return MemberCreateCommand
     */
    @Mapping(target = "name", source = "nickname")
    @Mapping(target = "imageUrl", source = "picture")
    MemberCreateCommand toCommand(OidcUserInfo oidcUserInfo);
}
