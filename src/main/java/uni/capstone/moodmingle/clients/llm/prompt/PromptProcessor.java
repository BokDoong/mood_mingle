package uni.capstone.moodmingle.clients.llm.prompt;

import lombok.experimental.UtilityClass;
import uni.capstone.moodmingle.clients.llm.gpt.dto.GptMessage;
import uni.capstone.moodmingle.domain.diary.application.dto.request.ReplyCreateCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * 퍼사드 패턴을 적용한 데이터 가공을 담당하는 Facade Class
 */
@UtilityClass
public class PromptProcessor {

    // 위로 답장 프롬프트 가공
    public static List<GptMessage> processLetterReplyPrompt(ReplyCreateCommand command) {
        String diaryPrompt = generateDiaryPrompt(command);
        String replyPrompt = ReplyPrompt.consolationPrompt;
        return processTotalPrompt(diaryPrompt, replyPrompt);
    }

    // 공감 답장 프롬프트 가공
    public static List<GptMessage> processSympathyReplyPrompt(ReplyCreateCommand command) {
        String diaryPrompt = generateDiaryPrompt(command);
        String replyPrompt = ReplyPrompt.sympathyPrompt;
        return processTotalPrompt(diaryPrompt, replyPrompt);
    }

    // 충고 답장 프롬프트 가공
    public static List<GptMessage> processAdviceReplyPrompt(ReplyCreateCommand command) {
        String diaryPrompt = generateDiaryPrompt(command);
        String replyPrompt = ReplyPrompt.advicePrompt;
        return processTotalPrompt(diaryPrompt, replyPrompt);
    }

    private String generateDiaryPrompt(ReplyCreateCommand command) {
        return "# 일기장\n- 작성자 이름 : " + command.memberName() + "\n" +
                "- 제목 : " + command.title() + "\n" +
                "- 오늘 하루의 감정 : " + command.emotion().getValue() + "\n" +
                "- 날짜 : " + command.date().toString() + "\n" +
                "- 내용 :\n" + command.content();
    }

    private List<GptMessage> processTotalPrompt(String diaryPrompt, String replyPrompt) {
        List<GptMessage> messages = new ArrayList<>();
        messages.add(new GptMessage("system", replyPrompt));
        messages.add(new GptMessage("user", diaryPrompt));

        return messages;
    }
}
