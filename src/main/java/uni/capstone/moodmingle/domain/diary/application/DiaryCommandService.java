package uni.capstone.moodmingle.domain.diary.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uni.capstone.moodmingle.clients.llm.LLMClient;
import uni.capstone.moodmingle.domain.diary.application.dto.DiaryCommandMapper;
import uni.capstone.moodmingle.domain.diary.application.dto.request.DiaryCreateCommand;
import uni.capstone.moodmingle.domain.diary.domain.Diary;
import uni.capstone.moodmingle.domain.diary.domain.DiaryRepository;
import uni.capstone.moodmingle.clients.s3.FileStore;
import uni.capstone.moodmingle.domain.diary.domain.Reply;
import uni.capstone.moodmingle.domain.diary.exception.DiaryAlreadyExistException;
import uni.capstone.moodmingle.domain.member.application.MemberQueryService;
import uni.capstone.moodmingle.domain.member.application.dto.response.SecretInfos;
import uni.capstone.moodmingle.domain.member.domain.Member;
import uni.capstone.moodmingle.global.error.ErrorCode;

/**
 * Diary 도메인에서 CRUD 를 진행하는 애플리케이션 서비스
 *
 * @author ijin
 */
@Service
@RequiredArgsConstructor
public class DiaryCommandService {

    private final DiaryRepository diaryRepository;
    private final MemberQueryService memberQueryService;

    private final DiaryCryptoHelper cryptoHelper;
    private final DiaryCommandMapper mapper;
    private final FileStore fileStore;
    private final LLMClient client;

    /**
     * 일기 저장 및 답변 요청
     *
     * @param command DiaryCreateCommand
     * @param type  답변 형식
     */
    @Transactional
    public void createAndSaveDiary(DiaryCreateCommand command, Reply.Type type) {
        // 사용자, 사용자의 비밀키, 초기벡터 조회
        Member member = findMember(command.memberId());
        SecretInfos secretInfos = findSecretInfos(member);

        // 암호화 및 Diary 생성
        Diary diary = createDiary(command, member, secretInfos);

        // 이미지 업로드 -> 저장
        uploadImageIfExisted(command, diary);
        saveDiary(member, diary);

        // 답변 요청
        replyDiary(command, type, member, secretInfos, diary);
    }

    private String getEncryptedContent(String content, SecretInfos secretInfos) {
        return cryptoHelper.encryptContent(secretInfos, content);
    }

    private void replyDiary(DiaryCreateCommand command, Reply.Type type, Member member, SecretInfos secretInfos, Diary diary) {
        switch (type) {
            case LETTER -> client.requestConsoleLetter(mapper.toCommand(command, member.getName()), diary.getId(), secretInfos);
            case ADVICE -> client.requestAdvicePhrase(mapper.toCommand(command, member.getName()), diary.getId(), secretInfos);
            case SYMPATHY -> client.requestSympathyPhrase(mapper.toCommand(command, member.getName()), diary.getId(), secretInfos);
        }
    }

    private SecretInfos findSecretInfos(Member member) {
        return memberQueryService.findMemberSecretInfos(member.getId());
    }

    private void uploadImageIfExisted(DiaryCreateCommand diaryCreateCommand, Diary diary) {
        if (!validateImageIncluded(diaryCreateCommand)) {
            String imageUrl = uploadImageToDB(diaryCreateCommand);
            diary.putImage(imageUrl);
        }
    }

    private String uploadImageToDB(DiaryCreateCommand diaryCreateCommand) {
        return fileStore.upload(diaryCreateCommand.image());
    }

    private boolean validateImageIncluded(DiaryCreateCommand diaryCreateCommand) {
        return diaryCreateCommand.image().isEmpty();
    }

    private void saveDiary(Member member, Diary diary) {
        member.addDiary(diary);
        diaryRepository.saveDiary(diary);
    }

    private Diary createDiary(DiaryCreateCommand command, Member member, SecretInfos secretInfos) {
        checkDiaryAlreadyExist(command, member);
        return mapper.toEntity(command, getEncryptedContent(command.content(), secretInfos), member);
    }

    private void checkDiaryAlreadyExist(DiaryCreateCommand command, Member member) {
        if (diaryRepository.checkDiaryAlreadyExist(member.getId(), command.date())) {
            throw new DiaryAlreadyExistException(ErrorCode.DIARY_ALREADY_EXIST);
        }
    }

    private Member findMember(Long memberId) {
        return memberQueryService.findMember(memberId);
    }
}
