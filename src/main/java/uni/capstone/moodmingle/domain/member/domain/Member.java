package uni.capstone.moodmingle.domain.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uni.capstone.moodmingle.domain.diary.domain.Diary;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Member 엔티티
 *
 * @author ijin
 */
@Entity
@Table(name = "tb_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    /**
     * 이름, 이메일, 비밀번호, 잠금 유무
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String name;
    @Column(unique = true, name = "email")
    private String email;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "private_key")
    private String privateKey;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Diary> diaries = new ArrayList<>();

    @Builder
    public Member(String name, String email, String imageUrl) {
        this.name = name;
        this.email = email;
        this.imageUrl = (imageUrl == null || imageUrl.isEmpty()) ? null : imageUrl;
        this.diaries = new ArrayList<>();
    }

    // 일기 추가
    public void addDiary(Diary diary) {
        diaries.add(diary);
    }

    // 개인키 생성
    public byte[] generateUserPrivateKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[32];
        secureRandom.nextBytes(keyBytes);
        return keyBytes;
    }

    // 암호화한 개인키 설정
    public void setEncryptedPrivateKey(String encryptedPrivateKey) {
        this.privateKey = encryptedPrivateKey;
    }
}
