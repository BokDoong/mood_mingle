package uni.capstone.moodmingle.diary.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uni.capstone.moodmingle.diary.application.dto.DiaryCommandMapper;
import uni.capstone.moodmingle.diary.application.dto.request.DiaryCreateCommand;
import uni.capstone.moodmingle.diary.domain.Diary;
import uni.capstone.moodmingle.diary.domain.DiaryRepository;
import uni.capstone.moodmingle.diary.domain.FileStore;
import uni.capstone.moodmingle.exception.BusinessException;
import uni.capstone.moodmingle.exception.code.ErrorCode;
import uni.capstone.moodmingle.member.application.MemberQueryService;
import uni.capstone.moodmingle.member.application.dto.response.SecretInfos;
import uni.capstone.moodmingle.member.domain.Member;

import static uni.capstone.moodmingle.diary.domain.Reply.*;

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
    private final ReplyManageService replyManageService;

    private final DiaryCryptoHelper cryptoHelper;
    private final DiaryCommandMapper mapper;
    private final FileStore fileStore;

    /**
     * 일기 저장 및 답변 요청
     *
     * @param command DiaryCreateCommand
     * @param type  답변 형식
     */
    @Transactional
    public void createAndSaveDiary(DiaryCreateCommand command, Type type) {
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

    private void replyDiary(DiaryCreateCommand command, Type type, Member member, SecretInfos secretInfos, Diary diary) {
        switch (type) {
            case LETTER -> createLetterResponse(command, member, diary, secretInfos);
            case ADVICE -> createAdviceResponse(command, member, diary, secretInfos);
            case SYMPATHY -> createSympathyResponse(command, member, diary, secretInfos);
        }
    }

    private SecretInfos findSecretInfos(Member member) {
        return memberQueryService.findMemberSecretInfos(member.getId());
    }

    private void createSympathyResponse(DiaryCreateCommand command, Member member, Diary diary, SecretInfos secretInfos) {
        replyManageService.replyBySympathyPhrase(mapper.toCommand(command, member.getName()), diary.getId(), secretInfos);
    }

    private void createLetterResponse(DiaryCreateCommand command, Member member, Diary diary, SecretInfos secretInfos) {
        replyManageService.replyByLetter(mapper.toCommand(command, member.getName()), diary.getId(), secretInfos);
    }

    private void createAdviceResponse(DiaryCreateCommand command, Member member, Diary diary, SecretInfos secretInfos) {
        replyManageService.replyByAdvice(mapper.toCommand(command, member.getName()), diary.getId(), secretInfos);
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
        return mapper.toEntity(command, getEncryptedContent(command, secretInfos), member);
    }

    private String getEncryptedContent(DiaryCreateCommand command, SecretInfos secretInfos) {
        return cryptoHelper.encryptContent(secretInfos, command.content());
    }

    private void checkDiaryAlreadyExist(DiaryCreateCommand command, Member member) {
        if (diaryRepository.checkDiaryAlreadyExist(member.getId(), command.date())) {
            throw new BusinessException(ErrorCode.DIARY_ALREADY_EXIST);
        }
    }

    private Member findMember(Long memberId) {
        return memberQueryService.findMember(memberId);
    }
}
