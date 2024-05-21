package uni.capstone.moodmingle.diary.application.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uni.capstone.moodmingle.diary.application.dto.PromptProcessingHelper;
import uni.capstone.moodmingle.diary.application.dto.request.ReplyCreateCommand;
import uni.capstone.moodmingle.diary.domain.prompt.DiaryPromptGenerator;
import uni.capstone.moodmingle.diary.domain.prompt.ReplyPromptGenerator;
import uni.capstone.moodmingle.diary.infra.dto.GptMessage;

import java.util.List;

/**
 * 퍼사드 패턴을 적용한 데이터 가공을 담당하는 Facade Class
 */
@Service
@RequiredArgsConstructor
public class PromptProcessingFacade {

    private final DiaryPromptGenerator diaryPromptGenerator;
    private final ReplyPromptGenerator replyPromptGenerator;

    /**
     * 위로 편지 답장을 받기 위한 프롬프트 메세지 가공
     *
     * @param command Reply 생성 CommandDTO
     * @return 가공된 Prompt Message
     */
    public List<GptMessage> processLetterReplyPrompt(ReplyCreateCommand command) {
        // 일기+답변 -> PromptMessage 가공
        String diaryPrompt = generateDiaryPrompt(command);
        String replyPrompt = generateLetterPrompt();

        // 최종 LLM Request Message 가공
        return PromptProcessingHelper.processPrompt(diaryPrompt, replyPrompt);
    }

    /**
     * 공감 답장을 받기 위한 프롬프트 메세지 가공
     *
     * @param command Reply 생성 CommandDTO
     * @return 가공된 Prompt Message
     */
    public List<GptMessage> processSympathyReplyPrompt(ReplyCreateCommand command) {
        // 일기+답변 -> PromptMessage 가공
        String diaryPrompt = generateDiaryPrompt(command);
        String replyPrompt = generateSympathyReplyPrompt();

        // 최종 LLM Request Message 가공
        return PromptProcessingHelper.processPrompt(diaryPrompt, replyPrompt);
    }

    /**
     * 공감 답장을 받기 위한 프롬프트 메세지 가공
     *
     * @param command Reply 생성 CommandDTO
     * @return 가공된 Prompt Message
     */
    public List<GptMessage> processAdviceReplyPrompt(ReplyCreateCommand command) {
        // 일기+답변 -> PromptMessage 가공
        String diaryPrompt = generateDiaryPrompt(command);
        String replyPrompt = generateAdviceReplyPrompt();

        // 최종 LLM Request Message 가공
        return PromptProcessingHelper.processPrompt(diaryPrompt, replyPrompt);
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
