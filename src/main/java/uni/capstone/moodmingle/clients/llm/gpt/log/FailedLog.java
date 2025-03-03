package uni.capstone.moodmingle.clients.llm.gpt.log;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "tb_failed_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FailedLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "thread_id")
    private String threadId;         // 요청 스레드 식별자 값
    @Column(name = "diary_id")
    private Long diaryId;
    @Lob
    @Column(name = "error_log")
    private String errorLog;         // 로그
    @CreationTimestamp
    private LocalDateTime time;

    @Builder
    public FailedLog(String threadId, String errorLog, Long diaryId) {
        this.threadId = threadId;
        this.diaryId = diaryId;
        this.errorLog = errorLog;
    }
}
