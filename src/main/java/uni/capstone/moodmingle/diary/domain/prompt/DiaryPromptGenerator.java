package uni.capstone.moodmingle.diary.domain.prompt;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
     * @param name 사용자명
     * @param title 제목
     * @param content 내용
     * @param dateTime 날짜
     * @return
     */
    public String generateDiaryPrompt(String name, String title, String content, LocalDate dateTime) {
        StringBuffer diaryBuffer = new StringBuffer();
        diaryBuffer.append("""
                # 일기장
                - 작성자 이름 : 
                """);
        diaryBuffer.append(name).append("\n");
        diaryBuffer.append("- 제목 : ").append(title).append("\n");
        diaryBuffer.append("- 날짜 : ").append(dateTime.toString()).append("\n");
        diaryBuffer.append("- 내용 :\n").append(content);

        return diaryBuffer.toString();
    }
}
