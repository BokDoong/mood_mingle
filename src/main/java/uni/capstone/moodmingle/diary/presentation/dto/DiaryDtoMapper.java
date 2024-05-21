package uni.capstone.moodmingle.diary.presentation.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.web.multipart.MultipartFile;
import uni.capstone.moodmingle.diary.application.dto.request.DiaryCreateCommand;
import uni.capstone.moodmingle.diary.presentation.dto.request.DiaryCreateDto;

/**
 * Client 로부터 받은 Presentation 계층의 DTO -> Application 계층의 DTO 로 바꿔주는 Mapper
 *
 * @author ijin
 */
@Mapper(componentModel = "spring")
public interface DiaryDtoMapper {
    DiaryDtoMapper INSTANCE = Mappers.getMapper(DiaryDtoMapper.class);

    @Mapping(target = "image", source = "image")
    DiaryCreateCommand toCommand(Long memberId, DiaryCreateDto diaryCreateDto, MultipartFile image);
}
