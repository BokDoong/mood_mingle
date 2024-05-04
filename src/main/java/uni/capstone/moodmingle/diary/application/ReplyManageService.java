package uni.capstone.moodmingle.diary.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uni.capstone.moodmingle.diary.application.dto.PromptProcessingHelper;
import uni.capstone.moodmingle.diary.infra.dto.Message;

import java.util.List;

/**
 * LLM 과 통신하는 어댑터에 프롬프트를 전달하여 답변 받아오는 애플리케이션 서비스
 *
 * @author ijin
 */
@Service
@RequiredArgsConstructor
public class ReplyManageService {

    private final LLMClient client;

    /**
     * 프롬프트 전달 및 답변 받기
     *
     * @param diaryPrompt 일기 프롬프트
     * @param replyPrompt 답장 프롬프트
     * @return LLM 답변
     */
    public String replyByLetter(String diaryPrompt, String replyPrompt) {
        // LLM Request Message 가공
        List<Message> prompts = PromptProcessingHelper.processReplyPrompt(diaryPrompt, replyPrompt);
        // LLMClient 에 요청
        return client.requestLetter(prompts);
    }
}
