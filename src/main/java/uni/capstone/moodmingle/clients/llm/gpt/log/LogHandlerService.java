package uni.capstone.moodmingle.clients.llm.gpt.log;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uni.capstone.moodmingle.clients.llm.gpt.dto.GptMessage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogHandlerService {

    private final FailedLogRepository failedLogRepository;

    // 요청 메세지 로깅
    public void logRequestMessages(List<GptMessage> messages) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n▶︎ GPT 요청 메세지\n");
        sb.append(messages.toString());
        log.info(sb.toString());
    }

    // 성공 메세지 로깅
    public void logSuccessMessages(String response) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n🟢 OpenAI API 성공 응답 \n");
        sb.append(response);
        log.info(sb.toString());
    }

    // 실패 메세지 로깅, 저장
    @Transactional
    public void logAndSaveErrorMessages(String requestedThreadId, Long diaryId, Throwable error) {
        log.error("\n❌ OpenAI API 오류 응답: " + error.getMessage());
        createAndSaveFailedLog(requestedThreadId, error.getMessage(), diaryId);
    }

    private void createAndSaveFailedLog(String requestThreadId, String log, Long diaryId) {
        FailedLog failedLog = FailedLog.builder()
                .requestedThreadId(requestThreadId)
                .errorLog(log)
                .diaryId(diaryId)
                .build();
        failedLogRepository.save(failedLog);
    }
}
