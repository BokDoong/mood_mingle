package uni.capstone.moodmingle.diary.domain.prompt;

import org.springframework.stereotype.Service;
import uni.capstone.moodmingle.diary.application.dto.request.ReplyCreateCommand;

/**
 * 회원의 정보를 받아 Diary Prompt Message 로 가공하는 도메인 서비스
 *
 * @author ijin
 */
@Service
public class DiaryPromptGenerator {
    /**
     * 일기 프롬프트 가공
     *
     * @param command Client 로부터 받은 일기장과 회원 이름
     * @return 일기 프롬프트
     */
    public String generateDiaryPrompt(ReplyCreateCommand command) {
        StringBuffer diaryBuffer = new StringBuffer();
        diaryBuffer.append("""
                # 일기장
                - 작성자 이름 : 
                """);
        diaryBuffer.append(command.memberName()).append("\n");
        diaryBuffer.append("- 제목 : ").append(command.title()).append("\n");
        diaryBuffer.append("- 오늘 하루의 감정 : ").append(command.emotion().getValue()).append("\n");
        diaryBuffer.append("- 날짜 : ").append(command.date().toString()).append("\n");
        diaryBuffer.append("- 내용 :\n").append(command.content());

        return diaryBuffer.toString();
    }
}
