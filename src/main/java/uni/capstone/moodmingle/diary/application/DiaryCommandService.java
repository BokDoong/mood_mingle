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
import uni.capstone.moodmingle.exception.NotFoundException;
import uni.capstone.moodmingle.exception.code.ErrorCode;
import uni.capstone.moodmingle.member.domain.Member;
import uni.capstone.moodmingle.member.domain.MemberRepository;

import java.util.concurrent.CompletableFuture;

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
    private final ReplyCommandService replyCommandService;

    private final DiaryCommandMapper mapper;
    private final FileStore fileStore;

    /**
     * 위로 답변 생성 및 일기+답변 저장
     */
    @Transactional
    public void replyDiaryWithLetter(DiaryCreateCommand command) {
        // 멤버 찾기
        Member member = findMember(command.memberId());

        // 일기 생성 및 이미지 업로드 -> 저장
        Diary diary = createDiary(command, member);
        uploadImageIfExisted(command, diary);
        saveDiary(member, diary);

        // 답변 요청
        createLetterResponse(command, member, diary);
    }

    /**
     * 충고 답변 생성 및 일기+답변 저장
     */
    public void replyDiaryWithAdvice(DiaryCreateCommand command) {
        // 멤버 찾기
        // 일기 생성(Diary)
        // 이미지 업로드 및 객체에 추가
        // 충고 답변 생성(LLM 에 요청) 및 추가
        // 일기+답변 저장
    }

    /**
     * 공감 답변 생성 및 일기+답변 저장
     */
    @Transactional
    public void replyDiaryWithSympathy(DiaryCreateCommand command) {
        // 멤버 찾기
        Member member = findMember(command.memberId());

        // 일기 생성 및 이미지 업로드 -> 저장
        Diary diary = createDiary(command, member);
        uploadImageIfExisted(command, diary);
        saveDiary(member, diary);

        // 답변 요청
        createSympathyResponse(command, member, diary);
    }

    private void createSympathyResponse(DiaryCreateCommand command, Member member, Diary diary) {
        CompletableFuture<String> llmAsyncTask = replyManageService.replyBySympathyPhrase(mapper.toCommand(command, member.getName()));
        llmAsyncTask.thenAccept(replyContent -> saveReply(diary, replyContent));
    }

    private void createLetterResponse(DiaryCreateCommand command, Member member, Diary diary) {
        CompletableFuture<String> llmAsyncTask = replyManageService.replyByLetter(mapper.toCommand(command, member.getName()));
        llmAsyncTask.thenAccept(replyContent -> saveReply(diary, replyContent));
    }

    private void saveReply(Diary diary, String replyContent) {
        replyCommandService.createAndSaveReply(diary.getId(), replyContent, Type.LETTER);
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
        return mapper.toEntity(command, member);
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND, memberId));
    }
}
