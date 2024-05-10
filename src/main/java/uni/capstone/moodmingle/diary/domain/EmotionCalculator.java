package uni.capstone.moodmingle.diary.domain;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static uni.capstone.moodmingle.diary.domain.Diary.*;

@Service
public class EmotionCalculator implements ApplicationListener<ContextRefreshedEvent> {

    private static HashMap<String, Integer> emotionsMap = new HashMap<>(10);

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
