package uni.capstone.moodmingle.diary.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uni.capstone.moodmingle.diary.application.dto.DiaryCommandMapper;
import uni.capstone.moodmingle.diary.application.dto.request.DiaryCreateCommand;
import uni.capstone.moodmingle.diary.domain.Diary;
import uni.capstone.moodmingle.diary.domain.DiaryRepository;
import uni.capstone.moodmingle.diary.domain.FileStore;
import uni.capstone.moodmingle.diary.domain.Reply;
import uni.capstone.moodmingle.diary.domain.prompt.DiaryPromptGenerator;
import uni.capstone.moodmingle.diary.domain.prompt.ReplyPromptGenerator;
import uni.capstone.moodmingle.exception.NotFoundException;
import uni.capstone.moodmingle.exception.code.ErrorCode;
import uni.capstone.moodmingle.member.domain.Member;
import uni.capstone.moodmingle.member.domain.MemberRepository;

import static uni.capstone.moodmingle.diary.domain.Reply.*;

/**
 * Diary 도메인에서 CRUD 를 진행하는 애플리케이션 서비스
 *
 * @author ijin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryCommandService {

    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;
    private final ReplyManageService replyManageService;
    private final FileStore fileStore;

    private final DiaryPromptGenerator diaryPromptGenerator;
    private final ReplyPromptGenerator replyPromptGenerator;

    private final DiaryCommandMapper mapper;

    /**
     * 위로 답변 생성 및 일기+답변 저장
     */
    @Transactional
    public void replyDiaryWithLetter(DiaryCreateCommand diaryCreateCommand) {
        // 멤버 찾기
        Member member = findMember(diaryCreateCommand);

        // 일기 생성(Diary)
        Diary diary = createDiary(diaryCreateCommand, member);
        validateAndUploadImage(diaryCreateCommand, diary);

        // 일기+답변 PromptMessage 데이터 가공
        String diaryPrompt = processDiaryPrompt(diaryCreateCommand, member);
        String replyPrompt = processLetterReplyPrompt();

        // ManageService 에 요청
        String replyContent = replyManageService.replyByLetter(diaryPrompt, replyPrompt);
        Reply reply = createReply(replyContent, Type.LETTER);

        // 일기 저장
        saveDiaryAndReply(member, diary, reply);
    }

    /**
     * 충고 답변 생성 및 일기+답변 저장
     */
    public void replyDiaryWithAdvice(DiaryCreateCommand diaryCreateCommand) {
        // 멤버 찾기
        // 일기 생성(Diary)
        // 이미지 업로드 및 객체에 추가
        // 충고 답변 생성(LLM 에 요청) 및 추가
        // 일기+답변 저장
    }

    /**
     * 공감 답변 생성 및 일기+답변 저장
     */
    public void replyDiaryWithSympathy(DiaryCreateCommand diaryCreateCommand) {
        // 멤버 찾기
        // 일기 생성(Diary)
        // 이미지 업로드 및 객체에 추가
        // 충고 답변 생성(LLM 에 요청) 및 추가
        // 일기+답변 저장
    }

    private void validateAndUploadImage(DiaryCreateCommand diaryCreateCommand, Diary diary) {
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

    private void saveDiaryAndReply(Member member, Diary diary, Reply reply) {
        diary.putReply(reply);
        member.addDiary(diary);
        diaryRepository.save(diary);
    }

    private Reply createReply(String replyContent, Type type) {
        return mapper.toEntity(replyContent, type);
    }

    private String processDiaryPrompt(DiaryCreateCommand diaryCreateCommand, Member member) {
        return diaryPromptGenerator.generateDiaryPrompt(member.getName(), diaryCreateCommand.title(), diaryCreateCommand.content(), diaryCreateCommand.date());
    }

    private Diary createDiary(DiaryCreateCommand command, Member member) {
        return mapper.toEntity(command, member);
    }

    private String processSympathyReplyPrompt(DiaryCreateCommand diaryCreateCommand) {
        return replyPromptGenerator.generateSympathyReplyPrompt(diaryCreateCommand.emotion());
    }

    private String processLetterReplyPrompt() {
        return replyPromptGenerator.generateLetterPrompt();
    }

    private Member findMember(DiaryCreateCommand diaryCreateCommand) {
        return memberRepository.findById(diaryCreateCommand.memberId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
