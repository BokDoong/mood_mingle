package uni.capstone.moodmingle.member.domain;

import jakarta.persistence.*;
import lombok.*;
import uni.capstone.moodmingle.diary.domain.Diary;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String name;
    @Column(unique = true, name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "is_locked")
    private boolean isLocked;
    @Enumerated(EnumType.STRING)
    @Column(name = "sns_type")
    private SnsType snsType;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Diary> diaries = new ArrayList<>();

    @Builder
    public Member(String name, String email, SnsType snsType) {
        this.name = name;
        this.email = email;
        this.snsType = snsType;
        isLocked = false;
    }

    @Getter
    @RequiredArgsConstructor
    public enum SnsType {
        KAKAO, NAVER,
    }
}
