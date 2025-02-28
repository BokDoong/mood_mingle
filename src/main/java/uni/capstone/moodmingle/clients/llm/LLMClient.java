package uni.capstone.moodmingle.clients.llm;

import uni.capstone.moodmingle.domain.diary.application.dto.request.ReplyCreateCommand;

/**
 * LLM 과 애플리케이션 서비스 간의 Port 역할을 하는 인터페이스
 *
 * @author ijin
 */
public interface LLMClient {

    // 위로 답장
    void requestConsoleLetter(ReplyCreateCommand command, Long diaryId);

    // 공감 답장
    void requestSympathyPhrase(ReplyCreateCommand command, Long diaryId);

    // 충고 답장
    void requestAdvicePhrase(ReplyCreateCommand command, Long diaryId);
}
