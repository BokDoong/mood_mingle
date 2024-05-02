package uni.capstone.moodmingle.common.basetime;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 엔티티의 BaseTime 정보를 담은 클래스
 *
 * @author ijin
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseTimeEntity {

    /**
     * 생성 시간
     */
    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * 변경 시간
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
