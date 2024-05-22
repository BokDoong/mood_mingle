package uni.capstone.moodmingle.config.jwt.oidc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uni.capstone.moodmingle.diary.domain.DiaryReplyCrypto;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        DiaryReplyCrypto.class
})
public class EncryptionTest {

    @Autowired
    private DiaryReplyCrypto diaryReplyCrypto;

    @Test
    public void 인크립터_테스트() {
        try {
            String plainText = "[시작] 😊옛날로 돌아가보면 사실 74 월드컵 네덜란드는 지금의 시선으로 보면 되게 형편없습니다. 보다가 계속 꺼버리고 결국엔 대부분의 경기들을 풀 타임 시청을 못하긴 했는데 (아리고 사키의 밀란까진 어떻게 됐는데 그 이전은 풀 타임 시청이 안 되더라구요. 원래 다시 보기라는 거 자체를 안 좋아하긴 하는데 옛날 경기 다 보고나서 다시 보기는 정말 할 게 못 된다는 걸 느끼고 그 후로 지나간 경기들이나 옛날 경기들은 안 찾아봅니다.) 모두가 미친듯이 뛰어다니긴 하는데 되게 무질서하고 중구난방이었습니다. 근데 당시에 충격적이었던 건 그거죠. 축구란 스포츠는 긴 거리를 돌파하고 상대 수비수들을 박스 근처에서 현란하게 제끼면서 골키퍼를 넘어서는 그런 스포츠였는데 다른 의미로 접근한 거였으니까요.그 덕을 두 번이나 본 바르셀로나 팬들은 유스에 미칠 수밖에 없지만 중요한 건 그것보다 어떻게 해야 계속 이걸 이어갈 수 있을까입니다. 물론 좋은 유스 선수들이 계속 튀어나오면 훨씬 수월하겠지만 그런 건 운이에요. 원래 바르셀로나는 주전급 선수들 중 유스의 비중이 별로 높았던 팀도 아니었고. 유스에 집착할 수밖에 없는 팀이지만 그게 적정 수준을 지키지 못하면 늘 위험할 수밖에 없달까요. 너무 없어도 안 되고 너무 많아도 안 되고. 얘기하고자 하는 건 유스 얘기가 아니니까 여기까지만 하고.미헬스는 당시 그 혹독한 훈련을 지시하면서 전술적 중심이던 크루이프와 한 번 더 떠올린 거죠. 어렸을 때부터 이런 걸 가르쳐서 적합한 시기에 어린 재능들을 올려서 담금질을 한다면 어떨까. 이런 시간들을 단축시킬 수 있지 않을까? 전 포지션으로 축구를 보는 걸 되게 싫어하고 멀리하는 편인데 왜 그러냐면 현대 축구에 와서 중요한 건 그런 게 아니기 때문입니다. 지금 대다수의 팀들의 축구를 볼 때 어떤 포지션에 누가 뛰고 있는데 누가 실력이 부족하다 이런 단편적인 관점보다는 볼을 소유한 상태일 때 최대한 효율적으로 선수들의 동선을 짜내고 수적 우위를 가져갈 수 있는 방법은 무엇이고 이들은 [끝]";
            String cypherText = diaryReplyCrypto.encrypt(plainText);

            log.info("1.암호화 : " + cypherText);
            log.info("2.복호화 : " + diaryReplyCrypto.decrypt(cypherText));
        } catch (Exception exception) {
            log.error(exception.toString());
        }
    }
}
