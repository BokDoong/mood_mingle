package uni.capstone.moodmingle.member.application.dto;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.member.application.dto.request.MemberCreateCommand;
import uni.capstone.moodmingle.member.domain.Member;
import uni.capstone.moodmingle.member.domain.Member.MemberBuilder;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-22T21:15:40+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.5 (JetBrains s.r.o.)"
)
@Component
public class MemberCommandMapperImpl implements MemberCommandMapper {

    @Override
    public Member toMember(MemberCreateCommand command) {
        if ( command == null ) {
            return null;
        }

        MemberBuilder member = Member.builder();

        member.name( command.name() );
        member.email( command.email() );
        member.imageUrl( command.imageUrl() );

        return member.build();
    }

    @Override
    public Member toMember(String name, String email) {
        if ( name == null && email == null ) {
            return null;
        }

        MemberBuilder member = Member.builder();

        if ( name != null ) {
            member.name( name );
        }
        if ( email != null ) {
            member.email( email );
        }

        return member.build();
    }
}
