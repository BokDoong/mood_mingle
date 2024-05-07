package uni.capstone.moodmingle.diary.application;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uni.capstone.moodmingle.diary.application.dto.request.ReplyCreateCommand;
import uni.capstone.moodmingle.diary.application.facade.PromptProcessingFacade;
import uni.capstone.moodmingle.diary.infra.dto.Message;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    public CompletableFuture<String> replyByLetter(ReplyCreateCommand command) {
        // LLM Request Message 가공
        List<Message> prompts = processingFacade.processLetterReplyPrompt(command);
        // LLMClient 에 요청
        return CompletableFuture.completedFuture(client.requestLetter(prompts));
    }

    /**
     * 프롬프트 전달 및 답변 받기
     *
     * @param command ReplyCreateCommand - DTO
     * @return LLM 답변
     */
    @Async("AsyncExecutor")
    public CompletableFuture<String> replyBySympathyPhrase(ReplyCreateCommand command) {
        // LLM Request Message 가공
        List<Message> prompts = processingFacade.processSympathyReplyPrompt(command);
        // LLMClient 에 요청
        return CompletableFuture.completedFuture(client.requestSympathyPhrase(prompts));
    }
}
