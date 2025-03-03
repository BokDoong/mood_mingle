package uni.capstone.moodmingle.clients.llm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uni.capstone.moodmingle.clients.llm.gpt.GptClient;
import uni.capstone.moodmingle.domain.diary.application.dto.request.ReplyCreateCommand;
import uni.capstone.moodmingle.domain.diary.domain.Reply;

/**
 * LLM 과 애플리케이션 서비스 간의 Port
 *
 * @author ijin
 */
@Component
@RequiredArgsConstructor
public class LLMClient {

    private final GptClient client;

    public void requestToLLMApi(ReplyCreateCommand command, Reply.Type type, Long diaryId) {
        switch (type) {
            case LETTER -> client.requestConsoleLetter(command, diaryId);
            case ADVICE -> client.requestAdvicePhrase(command, diaryId);
            case SYMPATHY -> client.requestSympathyPhrase(command, diaryId);
        }
    }
}
