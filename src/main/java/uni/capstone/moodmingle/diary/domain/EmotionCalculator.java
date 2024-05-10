package uni.capstone.moodmingle.diary.domain;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static uni.capstone.moodmingle.diary.domain.Diary.*;

/**
 * Emotion 인스턴스 데이터를 통계 및 계산하는 도메인 서비스
 *
 * @author ijin
 */
@Service
public class EmotionCalculator implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * makeStatisticsOfEmotions() 를 계산하기 위해 복사를 하기 위한 해쉬맵
     */
    private static HashMap<String, Integer> emotionsMap = new HashMap<>(10);

    /**
     * 서버가 실행되면 emotionsMap 안에 Emotion Value 를 넣어 초기화
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Arrays.stream(Emotion.values())
                .toList()
                .forEach(emotion -> emotionsMap.put(emotion.name(), 0));
    }

    /**
     * 기간 별 감정의 통계 조회
     *
     * @param emotionCountInfos DB 에서 기간별 조회된 감정 리스트
     */
    public HashMap<String, Integer> makeStatisticsOfEmotions(List<Emotion> emotionCountInfos) {
        HashMap<String, Integer> emotionCountStatistics = new HashMap<>(emotionsMap);

        emotionCountInfos.forEach(
                emotion -> {
                    int count = emotionCountStatistics.get(emotion.name());
                    emotionCountStatistics.put(emotion.name(), count + 1);
                }
        );

        return emotionCountStatistics;
    }
}
