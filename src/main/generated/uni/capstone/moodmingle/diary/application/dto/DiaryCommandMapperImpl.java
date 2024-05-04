package uni.capstone.moodmingle.diary.application.dto;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.diary.application.dto.request.DiaryCreateCommand;
import uni.capstone.moodmingle.diary.domain.Diary;
import uni.capstone.moodmingle.diary.domain.Diary.DiaryBuilder;
import uni.capstone.moodmingle.diary.domain.Reply;
import uni.capstone.moodmingle.diary.domain.Reply.ReplyBuilder;
import uni.capstone.moodmingle.diary.domain.Reply.Type;
import uni.capstone.moodmingle.member.domain.Member;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-04T10:42:59+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.5 (JetBrains s.r.o.)"
)
@Component
public class DiaryCommandMapperImpl implements DiaryCommandMapper {

    @Override
    public Diary toEntity(DiaryCreateCommand diaryCreateCommand, Member member) {
        if ( diaryCreateCommand == null && member == null ) {
            return null;
        }

        DiaryBuilder diary = Diary.builder();

        if ( diaryCreateCommand != null ) {
            diary.title( diaryCreateCommand.title() );
            diary.date( diaryCreateCommand.date() );
            diary.content( diaryCreateCommand.content() );
            diary.emotion( diaryCreateCommand.emotion() );
            diary.weather( diaryCreateCommand.weather() );
        }
        if ( member != null ) {
            diary.member( member );
        }

        return diary.build();
    }

    @Override
    public Reply toEntity(String replyContent, Type type) {
        if ( replyContent == null && type == null ) {
            return null;
        }

        ReplyBuilder reply = Reply.builder();

        if ( replyContent != null ) {
            reply.content( replyContent );
        }
        if ( type != null ) {
            reply.type( type );
        }

        return reply.build();
    }
}
