package uni.capstone.moodmingle.diary.application;

import uni.capstone.moodmingle.diary.infra.dto.GptMessage;
import uni.capstone.moodmingle.member.application.dto.response.SecretInfos;
import uni.capstone.moodmingle.member.domain.MemberSecretInfo;

import java.util.List;

/**
 * LLM 과 애플리케이션 서비스 간의 Port 역할을 하는 인터페이스
 *
 * @author ijin
 */
public interface LLMClient {
    /**
     * 위로편지 요청
     *
     * @param prompts Request Prompt Messages
     * @param diaryId Diary's Id
     */
    void requestLetter(List<GptMessage> prompts, Long diaryId, SecretInfos secretInfos);

    /**
     * 공감 답변 요청
     *
     * @param prompts Request Prompt Messages
     * @param diaryId Diary's Id
     */
    void requestSympathyPhrase(List<GptMessage> prompts, Long diaryId, SecretInfos secretInfos);

    /**
     * 충고 답변 요청
     *
     * @param prompts Request Prompt Messages
     * @param diaryId Diary's Id
     */
    void requestAdvicePhrase(List<GptMessage> prompts, Long diaryId, SecretInfos secretInfos);
}
