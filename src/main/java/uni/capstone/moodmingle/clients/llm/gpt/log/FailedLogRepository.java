package uni.capstone.moodmingle.clients.llm.gpt.log;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional
public class FailedLogRepository {

    private final EntityManager em;

    public void save(FailedLog log) {
        em.persist(log);
    }
}
