package uni.capstone.moodmingle.domain.member.presentation.dto;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.domain.member.application.dto.request.MemberCreateCommand;
import uni.capstone.moodmingle.domain.member.presentation.dto.request.MemberCreateDto;
import uni.capstone.moodmingle.global.security.oidc.entity.OidcUserInfo;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-02T20:09:10+0900",
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
