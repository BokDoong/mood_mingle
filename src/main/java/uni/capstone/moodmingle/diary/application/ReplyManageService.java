package uni.capstone.moodmingle.diary.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uni.capstone.moodmingle.diary.application.dto.request.ReplyCreateCommand;
import uni.capstone.moodmingle.diary.application.facade.PromptProcessingFacade;
import uni.capstone.moodmingle.diary.infra.dto.GptMessage;

import java.util.List;

/**
 * LLM 과 통신하는 어댑터에 프롬프트를 전달하여 답변 받아오는 애플리케이션 서비스
 *
 * @author ijin
 */
@Service
@RequiredArgsConstructor
public class ReplyManageService {

    private final PromptProcessingFacade processingFacade;
    private final LLMClient client;

    /**
     * 프롬프트 전달 및 답변 받기
     *
     * @param command ReplyCreateCommand - DTO
     * @return LLM 답변
     */
    @Async("AsyncExecutor")
    public void replyByLetter(ReplyCreateCommand command, Long diaryId) {
        // LLM Request Message 가공
        List<GptMessage> prompts = processingFacade.processLetterReplyPrompt(command);
        // LLMClient 에 요청
        client.requestLetter(prompts, diaryId);
    }
    /**
     * 프롬프트 전달 및 답변 받기
     *
     * @param command ReplyCreateCommand - DTO
     * @return LLM 답변
     */
    @Async("AsyncExecutor")
    public void replyBySympathyPhrase(ReplyCreateCommand command, Long diaryId) {
        // LLM Request Message 가공
        List<GptMessage> prompts = processingFacade.processSympathyReplyPrompt(command);
        // LLMClient 에 요청
        client.requestSympathyPhrase(prompts, diaryId);
    }
}
