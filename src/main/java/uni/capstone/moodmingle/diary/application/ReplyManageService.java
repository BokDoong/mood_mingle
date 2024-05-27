package uni.capstone.moodmingle.diary.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uni.capstone.moodmingle.diary.application.dto.request.ReplyCreateCommand;
import uni.capstone.moodmingle.diary.application.facade.PromptProcessingFacade;
import uni.capstone.moodmingle.diary.infra.dto.GptMessage;
import uni.capstone.moodmingle.member.application.dto.response.SecretInfos;
import uni.capstone.moodmingle.member.domain.MemberSecretInfo;

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
     * 프롬프트 전달 및 위로편지 답변 받기
     *
     * @param command ReplyCreateCommand - DTO
     * @return LLM 답변
     */
    @Async("AsyncExecutor")
    public void replyByLetter(ReplyCreateCommand command, Long diaryId, SecretInfos secretInfos) {
        // LLM Request Message 가공
        List<GptMessage> prompts = processingFacade.processLetterReplyPrompt(command);
        // LLMClient 에 요청
        client.requestLetter(prompts, diaryId, secretInfos);
    }

    /**
     * 프롬프트 전달 및 공감 답변 받기
     *
     * @param command ReplyCreateCommand - DTO
     * @return LLM 답변
     */
    @Async("AsyncExecutor")
    public void replyBySympathyPhrase(ReplyCreateCommand command, Long diaryId, SecretInfos secretInfos) {
        // LLM Request Message 가공
        List<GptMessage> prompts = processingFacade.processSympathyReplyPrompt(command);
        // LLMClient 에 요청
        client.requestSympathyPhrase(prompts, diaryId, secretInfos);
    }

    /**
     * 프롬프트 전달 및 충고 답변 받기
     *
     * @param command ReplyCreateCommand - DTO
     * @return LLM 답변
     */
    @Async("AsyncExecutor")
    public void replyByAdvice(ReplyCreateCommand command, Long diaryId, SecretInfos secretInfos) {
        // LLM Request Message 가공
        List<GptMessage> prompts = processingFacade.processAdviceReplyPrompt(command);
        // LLMClient 에 요청
        client.requestAdvicePhrase(prompts, diaryId, secretInfos);
    }
}
