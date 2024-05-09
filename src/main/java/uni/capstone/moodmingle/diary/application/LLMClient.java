package uni.capstone.moodmingle.diary.application;

import uni.capstone.moodmingle.diary.infra.dto.GptMessage;

import java.util.List;

/**
 * LLM 과 애플리케이션 서비스 간의 Port 역할을 하는 인터페이스
 *
 * @author ijin
 */
public interface LLMClient {
    /**
     * 위로편지 요청
     *
     * @param prompts Request Prompt Messages
     * @return LLM Response
     */
    String requestLetter(List<GptMessage> prompts);

    /**
     * 공감 답변 요청
     *
     * @param prompts Request Prompt Messages
     * @return LLM Response
     */
    String requestSympathyPhrase(List<GptMessage> prompts);
}
