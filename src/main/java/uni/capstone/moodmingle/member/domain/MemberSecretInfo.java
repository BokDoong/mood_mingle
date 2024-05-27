package uni.capstone.moodmingle.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 멤버의 비밀키, 초기 벡터 저장할 테이블
 *
 * @author ijin
 */
@Entity
@Table(name = "tb_member_secret_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberSecretInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "member_id")
    private long memberId;
    @Lob
    @Column(name = "secret_key")
    private byte[] secretKey;
    @Lob
    @Column(name = "initial_vector")
    private byte[] iv;

    @Builder
    public MemberSecretInfo(long memberId, byte[] secretKey, byte[] iv) {
        this.memberId = memberId;
        this.secretKey = secretKey;
        this.iv = iv;
    }
}
