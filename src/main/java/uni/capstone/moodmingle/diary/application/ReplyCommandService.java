package uni.capstone.moodmingle.diary.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uni.capstone.moodmingle.diary.application.dto.DiaryCommandMapper;
import uni.capstone.moodmingle.diary.application.dto.request.DiaryCreateCommand;
import uni.capstone.moodmingle.diary.domain.Diary;
import uni.capstone.moodmingle.diary.domain.DiaryRepository;
import uni.capstone.moodmingle.diary.domain.Reply;
import uni.capstone.moodmingle.exception.NotFoundException;
import uni.capstone.moodmingle.exception.code.ErrorCode;
import uni.capstone.moodmingle.member.application.dto.response.SecretInfos;
import uni.capstone.moodmingle.member.domain.MemberSecretInfo;

import static uni.capstone.moodmingle.diary.domain.Reply.*;

/**
 * Reply 도메인에서 CRUD 를 진행하는 애플리케이션 서비스
 *
 * @author ijin
 */
@Service
@RequiredArgsConstructor
public class ReplyCommandService {

    private final DiaryRepository diaryRepository;

    private final DiaryCryptoHelper cryptoHelper;
    private final DiaryCommandMapper mapper;

    /**
     * Reply 를 생성하고, 저장
     *
     * @param diaryId 답장될 일기 ID
     * @param replyContent 답장 내용
     * @param type 답장 Type
     */
    @Transactional
    public void createAndSaveReply(Long diaryId, String replyContent, Type type, SecretInfos secretInfo) {
        // Diary 찾기
        Diary diary = findDiary(diaryId);

        // Reply 생성 및 저장
        Reply reply = createReply(replyContent, type, secretInfo);
        saveReply(diary, reply);
    }

    private void saveReply(Diary diary, Reply reply) {
        diary.putReply(reply);
        diaryRepository.saveReply(reply);
    }

    private Diary findDiary(Long diaryId) {
        return diaryRepository.findById(diaryId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND, diaryId));
    }

    private Reply createReply(String replyContent, Type type, SecretInfos secretInfos) {
        return mapper.toEntity(getEncryptedContent(replyContent, secretInfos), type);
    }

    private String getEncryptedContent(String replyContent, SecretInfos secretInfos) {
        return cryptoHelper.encryptContent(secretInfos, replyContent);
    }
}
