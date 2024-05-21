package uni.capstone.moodmingle.diary.application.dto;

import lombok.experimental.UtilityClass;
import uni.capstone.moodmingle.diary.infra.dto.GptMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 일기+답변 프롬프트로 LLM 에 최종적으로 전달하기 위해 DTO 를 가공하는 유틸리티 클래스
 *
 * @author ijin
 */
@UtilityClass
public class PromptProcessingHelper {

    /**
     * 프롬프트 가공
     *
     * @param diaryPrompt user Role 에 넣을 일기 프롬프트
     * @param replyPrompt system Role 에 넣을 답변 프롬프
     * @return 가공된 PromptMessage 객체 리스트
     */
    public List<GptMessage> processPrompt(String diaryPrompt, String replyPrompt) {
        List<GptMessage> messages = new ArrayList<>();
        messages.add(new GptMessage("system", replyPrompt));
        messages.add(new GptMessage("user", diaryPrompt));

        return messages;
    }
}
