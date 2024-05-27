package uni.capstone.moodmingle.member.application.dto;

import java.util.Arrays;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.member.application.dto.request.MemberCreateCommand;
import uni.capstone.moodmingle.member.domain.Member;
import uni.capstone.moodmingle.member.domain.Member.MemberBuilder;
import uni.capstone.moodmingle.member.domain.MemberSecretInfo;
import uni.capstone.moodmingle.member.domain.MemberSecretInfo.MemberSecretInfoBuilder;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-27T15:28:57+0900",
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

    @Override
    public MemberSecretInfo toSecretInfo(Long memberId, byte[] secretKey, byte[] iv) {
        if ( memberId == null && secretKey == null && iv == null ) {
            return null;
        }

        MemberSecretInfoBuilder memberSecretInfo = MemberSecretInfo.builder();

        if ( memberId != null ) {
            memberSecretInfo.memberId( memberId );
        }
        if ( secretKey != null ) {
            byte[] secretKey1 = secretKey;
            if ( secretKey1 != null ) {
                memberSecretInfo.secretKey( Arrays.copyOf( secretKey1, secretKey1.length ) );
            }
        }
        if ( iv != null ) {
            byte[] iv1 = iv;
            if ( iv1 != null ) {
                memberSecretInfo.iv( Arrays.copyOf( iv1, iv1.length ) );
            }
        }

        return memberSecretInfo.build();
    }
}
