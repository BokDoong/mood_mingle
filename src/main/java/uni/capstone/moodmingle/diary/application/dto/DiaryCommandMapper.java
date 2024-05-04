package uni.capstone.moodmingle.diary.application.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import uni.capstone.moodmingle.diary.application.dto.request.DiaryCreateCommand;
import uni.capstone.moodmingle.diary.domain.Diary;
import uni.capstone.moodmingle.diary.domain.Reply;
import uni.capstone.moodmingle.member.domain.Member;

import static uni.capstone.moodmingle.diary.domain.Reply.*;

/**
 * Application 계층의 DTO 를 Entity 로 바꾸는 Mapper
 *
 * @author ijin
 */
@Mapper(componentModel = "spring")
public interface DiaryCommandMapper {
    DiaryCommandMapper INSTANCE = Mappers.getMapper(DiaryCommandMapper.class);

    /**
     * DTO & Member -> Diary 매핑
     *
     * @param diaryCreateCommand 일기 생성 Command DTO
     * @param member Member
     * @return Diary 객체
     */
    @Mapping(target = "member", source = "member")
    Diary toEntity(DiaryCreateCommand diaryCreateCommand, Member member);

    /**
     * Content & Type -> Reply 매핑
     *
     * @param replyContent LLM 으로부터 받은 Reply 내용물
     * @param type Reply Type
     * @return Reply 객체
     */
    @Mapping(target = "content", source = "replyContent")
    Reply toEntity(String replyContent, Type type);

}
