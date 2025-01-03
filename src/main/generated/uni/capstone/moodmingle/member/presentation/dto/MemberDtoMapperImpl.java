package uni.capstone.moodmingle.member.presentation.dto;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.config.security.oidc.entity.OidcUserInfo;
import uni.capstone.moodmingle.member.application.dto.request.MemberCreateCommand;
import uni.capstone.moodmingle.member.presentation.dto.request.MemberCreateDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-02T17:57:41+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.11 (Amazon.com Inc.)"
)
@Component
public class MemberDtoMapperImpl implements MemberDtoMapper {

    @Override
    public MemberCreateCommand toCommand(OidcUserInfo oidcUserInfo) {
        if ( oidcUserInfo == null ) {
            return null;
        }

        String name = null;
        String imageUrl = null;
        String email = null;

        name = oidcUserInfo.getNickname();
        imageUrl = oidcUserInfo.getPicture();
        email = oidcUserInfo.getEmail();

        MemberCreateCommand memberCreateCommand = new MemberCreateCommand( name, email, imageUrl );

        return memberCreateCommand;
    }

    @Override
    public MemberCreateCommand toCommand(MemberCreateDto memberCreateDto) {
        if ( memberCreateDto == null ) {
            return null;
        }

        String email = null;
        String name = null;
        String imageUrl = null;

        email = memberCreateDto.getEmail();
        name = memberCreateDto.getName();
        imageUrl = memberCreateDto.getImageUrl();

        MemberCreateCommand memberCreateCommand = new MemberCreateCommand( name, email, imageUrl );

        return memberCreateCommand;
    }
}
