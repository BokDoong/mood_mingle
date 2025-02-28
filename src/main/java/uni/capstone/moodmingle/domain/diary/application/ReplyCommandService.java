package uni.capstone.moodmingle.domain.diary.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uni.capstone.moodmingle.domain.diary.application.dto.DiaryCommandMapper;
import uni.capstone.moodmingle.domain.diary.domain.Diary;
import uni.capstone.moodmingle.domain.diary.domain.DiaryCrypto;
import uni.capstone.moodmingle.domain.diary.domain.DiaryRepository;
import uni.capstone.moodmingle.domain.diary.domain.Reply;
import uni.capstone.moodmingle.domain.member.domain.Member;
import uni.capstone.moodmingle.domain.member.exception.MemberNotFoundException;
import uni.capstone.moodmingle.global.error.ErrorCode;

/**
 * Reply 도메인에서 CRUD 를 진행하는 애플리케이션 서비스
 *
 * @author ijin
 */
@Service
@RequiredArgsConstructor
public class ReplyCommandService {

    private final DiaryRepository diaryRepository;
    private final DiaryCrypto diaryCrypto;
    private final DiaryCommandMapper mapper;

    /**
     * Reply 를 생성하고, 저장
     *
     * @param diaryId 답장될 일기 ID
     * @param replyContent 답장 내용
     * @param type 답장 Type
     */
    @Transactional
    public void createAndSaveReply(Long diaryId, String replyContent, Reply.Type type) {
        // Diary, Member 찾기
        Diary diary = findDiary(diaryId);
        Member member = diary.getMember();

        // Reply 생성 및 저장
        Reply reply = createReply(replyContent, type, member.getSecretKey());
        saveReply(diary, reply);
    }

    private void saveReply(Diary diary, Reply reply) {
        diary.putReply(reply);
        diaryRepository.saveReply(reply);
    }

    private Diary findDiary(Long diaryId) {
        return diaryRepository.findById(diaryId)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND, diaryId));
    }

    private Reply createReply(String replyContent, Reply.Type type, String privateKey) {
        return mapper.toEntity(getEncryptedContent(replyContent, privateKey), type);
    }

    private String getEncryptedContent(String replyContent, String privateKey) {
        return diaryCrypto.encrypt(privateKey, replyContent);
    }
}
