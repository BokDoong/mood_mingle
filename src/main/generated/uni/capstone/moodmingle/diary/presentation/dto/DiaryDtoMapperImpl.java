package uni.capstone.moodmingle.diary.presentation.dto;

import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import uni.capstone.moodmingle.diary.application.dto.request.DiaryCreateCommand;
import uni.capstone.moodmingle.diary.domain.Diary.Emotion;
import uni.capstone.moodmingle.diary.domain.Diary.Weather;
import uni.capstone.moodmingle.diary.presentation.dto.request.DiaryCreateDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-27T15:28:57+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.5 (JetBrains s.r.o.)"
)
@Component
public class DiaryDtoMapperImpl implements DiaryDtoMapper {

    @Override
    public DiaryCreateCommand toCommand(Long memberId, DiaryCreateDto diaryCreateDto, MultipartFile image) {
        if ( memberId == null && diaryCreateDto == null && image == null ) {
            return null;
        }

        Long memberId1 = null;
        if ( memberId != null ) {
            memberId1 = memberId;
        }
        String title = null;
        String content = null;
        LocalDate date = null;
        Emotion emotion = null;
        Weather weather = null;
        if ( diaryCreateDto != null ) {
            title = diaryCreateDto.getTitle();
            content = diaryCreateDto.getContent();
            date = diaryCreateDto.getDate();
            emotion = diaryCreateDto.getEmotion();
            weather = diaryCreateDto.getWeather();
        }
        MultipartFile image1 = null;
        if ( image != null ) {
            image1 = image;
        }

        DiaryCreateCommand diaryCreateCommand = new DiaryCreateCommand( memberId1, title, content, date, emotion, weather, image1 );

        return diaryCreateCommand;
    }
}
