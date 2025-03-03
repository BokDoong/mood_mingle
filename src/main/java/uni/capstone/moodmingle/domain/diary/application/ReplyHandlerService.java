package uni.capstone.moodmingle.domain.diary.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uni.capstone.moodmingle.domain.diary.application.dto.DiaryCommandMapper;
import uni.capstone.moodmingle.domain.diary.domain.Diary;
import uni.capstone.moodmingle.domain.diary.domain.DiaryCrypto;
import uni.capstone.moodmingle.domain.diary.domain.DiaryRepository;
import uni.capstone.moodmingle.domain.diary.domain.Reply;
import uni.capstone.moodmingle.domain.member.application.MemberQueryService;
import uni.capstone.moodmingle.domain.member.domain.Member;

/**
 * Reply 도메인에서 CRUD 를 진행하는 애플리케이션 서비스
 *
 * @author ijin
 */
@Service
@RequiredArgsConstructor
public class ReplyHandlerService {

    private final MemberQueryService memberQueryService;
    private final DiaryQueryService diaryQueryService;
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
        byte[] privateKey = memberQueryService.getDecryptedPrivateKey(member.getPrivateKey());

        // Reply 생성 및 저장
        Reply reply = createReply(replyContent, type, privateKey);
        saveReply(diary, reply);
    }

    /**
     * LLM 에 요청 실패한 일기 상태 수정
     *
     * @param diaryId 일기 ID
     */
    @Transactional
    public void treatFailedReplyDiary(Long diaryId) {
        Diary diary = findDiary(diaryId);
        diary.failRepliedStatus();
    }

    private void saveReply(Diary diary, Reply reply) {
        diary.putReply(reply);
        diaryRepository.saveReply(reply);
    }

    private Diary findDiary(Long diaryId) {
        return diaryQueryService.getDiaryById(diaryId);
    }

    private Reply createReply(String replyContent, Reply.Type type, byte[] privateKey) {
        return mapper.toEntity(diaryCrypto.encrypt(privateKey, replyContent), type);
    }
}
