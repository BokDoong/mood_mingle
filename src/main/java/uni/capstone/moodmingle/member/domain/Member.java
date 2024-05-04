package uni.capstone.moodmingle.member.domain;

import jakarta.persistence.*;
import lombok.*;
import uni.capstone.moodmingle.diary.domain.Diary;

import java.util.ArrayList;
import java.util.List;

/**
 * Member 엔티티
 *
 * @author ijin
 */
@Entity
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
    @Column(name = "password")
    private String password;
    @Column(name = "is_locked")
    private boolean isLocked;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Diary> diaries = new ArrayList<>();

    @Builder
    public Member(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isLocked = false;
    }

    public void addDiary(Diary diary) {
        diaries.add(diary);
    }
}
