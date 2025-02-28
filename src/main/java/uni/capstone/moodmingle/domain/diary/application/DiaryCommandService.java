package uni.capstone.moodmingle.domain.diary.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uni.capstone.moodmingle.clients.aws.s3.FileStore;
import uni.capstone.moodmingle.clients.llm.LLMClient;
import uni.capstone.moodmingle.domain.diary.application.dto.DiaryCommandMapper;
import uni.capstone.moodmingle.domain.diary.application.dto.request.DiaryCreateCommand;
import uni.capstone.moodmingle.domain.diary.domain.Diary;
import uni.capstone.moodmingle.domain.diary.domain.DiaryCrypto;
import uni.capstone.moodmingle.domain.diary.domain.DiaryRepository;
import uni.capstone.moodmingle.domain.diary.domain.Reply;
import uni.capstone.moodmingle.domain.diary.exception.DiaryAlreadyExistException;
import uni.capstone.moodmingle.domain.member.application.MemberQueryService;
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

    private final DiaryCrypto diaryCrypto;
    private final MemberQueryService memberQueryService;
    private final DiaryRepository diaryRepository;
    private final DiaryCommandMapper mapper;
    private final FileStore fileStore;
    private final LLMClient client;


    /**
     * 일기 작성
     *
     * @param command 일기 생성 DTO
     * @param type 일기 타입
     */
    @Transactional
    public void createAndSaveDiary(DiaryCreateCommand command, Reply.Type type) {
        // 사용자, 사용자의 비밀키, 초기벡터 조회
        Member member = findMember(command.memberId());
        // 암호화 및 Diary 생성
        Diary diary = createDiary(command, member);
        // 이미지 업로드 -> 저장
        uploadImageIfExisted(command, diary);
        saveDiary(member, diary);
        // 답변 요청
        replyDiary(command, type, member, diary);
    }

    private void replyDiary(DiaryCreateCommand command, Reply.Type type, Member member, Diary diary) {
        switch (type) {
            case LETTER -> client.requestConsoleLetter(mapper.toCommand(command, member.getName()), diary.getId());
            case ADVICE -> client.requestAdvicePhrase(mapper.toCommand(command, member.getName()), diary.getId());
            case SYMPATHY -> client.requestSympathyPhrase(mapper.toCommand(command, member.getName()), diary.getId());
        }
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

    private Diary createDiary(DiaryCreateCommand command, Member member) {
        checkDiaryAlreadyExist(command, member);
        byte[] privateKey = memberQueryService.getDecryptedPrivateKey(member.getPrivateKey());
        return mapper.toEntity(command, diaryCrypto.encrypt(privateKey, command.content()), member);
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
