package uni.capstone.moodmingle.member.presentation.dto;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.config.security.oidc.entity.OidcUserInfo;
import uni.capstone.moodmingle.member.application.dto.request.MemberCreateCommand;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-21T16:15:50+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.5 (JetBrains s.r.o.)"
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
}
