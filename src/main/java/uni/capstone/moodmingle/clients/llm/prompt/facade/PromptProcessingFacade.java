package uni.capstone.moodmingle.clients.llm.prompt.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uni.capstone.moodmingle.clients.llm.prompt.GptPromptProcessingHelper;
import uni.capstone.moodmingle.domain.diary.application.dto.request.ReplyCreateCommand;
import uni.capstone.moodmingle.clients.llm.prompt.DiaryPromptGenerator;
import uni.capstone.moodmingle.clients.llm.prompt.ReplyPromptGenerator;
import uni.capstone.moodmingle.clients.llm.gpt.dto.GptMessage;

import java.util.List;

/**
 * 퍼사드 패턴을 적용한 데이터 가공을 담당하는 Facade Class
 */
@Service
@RequiredArgsConstructor
public class PromptProcessingFacade {

    private final GptPromptProcessingHelper gptPromptProcessingHelper;
    private final DiaryPromptGenerator diaryPromptGenerator;
    private final ReplyPromptGenerator replyPromptGenerator;

    // 위로 답장 프롬프트 가공
    public List<GptMessage> processLetterReplyPrompt(ReplyCreateCommand command) {
        String diaryPrompt = generateDiaryPrompt(command);
        String replyPrompt = generateLetterPrompt();
        return processGptMessages(diaryPrompt, replyPrompt);
    }

    // 공감 답장 프롬프트 가공
    public List<GptMessage> processSympathyReplyPrompt(ReplyCreateCommand command) {
        String diaryPrompt = generateDiaryPrompt(command);
        String replyPrompt = generateSympathyReplyPrompt();
        return processGptMessages(diaryPrompt, replyPrompt);
    }

    // 충고 답장 프롬프트 가공
    public List<GptMessage> processAdviceReplyPrompt(ReplyCreateCommand command) {
        String diaryPrompt = generateDiaryPrompt(command);
        String replyPrompt = generateAdviceReplyPrompt();
        return processGptMessages(diaryPrompt, replyPrompt);
    }

    private List<GptMessage> processGptMessages(String diaryPrompt, String replyPrompt) {
        return gptPromptProcessingHelper.processPrompt(diaryPrompt, replyPrompt);
    }

    private String generateAdviceReplyPrompt() {
        return replyPromptGenerator.processAdviceReplyPrompt();
    }

    private String generateSympathyReplyPrompt() {
        return replyPromptGenerator.generateSympathyReplyPrompt();
    }

    private String generateLetterPrompt() {
        return replyPromptGenerator.generateLetterPrompt();
    }

    private String generateDiaryPrompt(ReplyCreateCommand command) {
        return diaryPromptGenerator.generateDiaryPrompt(command);
    }
}
