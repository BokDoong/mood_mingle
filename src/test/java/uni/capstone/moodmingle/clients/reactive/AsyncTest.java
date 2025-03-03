package uni.capstone.moodmingle.clients.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

public class AsyncTest {

    @Test
    public void Mono_테스트() {
        // Mono Publisher 생성해서 Subscriber 로 핸들링해보기
        Mono<String> mono = Mono.just("Hello, Mono!");
        mono.subscribe(data -> System.out.println("모노보노: " + data));

        // 핸들링
        Mono<String> emptyMono = Mono.empty();
        emptyMono.subscribe(
                data -> System.out.println("응답: " + data),
                error -> System.out.println("에러: " + error),
                () -> System.out.println("완료!")
        );

        // Mono 는 데이터를 변환하거나 가공하기 위해 다양한 연산자 제공
        // 1. map
        Mono<String> lowerMono = Mono.just("HELLO")
                .map(data -> data.toLowerCase());
        lowerMono.subscribe(data -> System.out.println("응답 : " + data));
        // 2. flatMap
        Mono<String> upperMono = Mono.just("hello")
                .flatMap(data -> Mono.just(data.toUpperCase()));
        upperMono.subscribe(data -> System.out.println("응답 : " + data));
        // 3. filter
        Mono<String> filterMono = Mono.just("hello")
                .filter(data -> data.length() > 10);
        filterMono.subscribe(
                data -> System.out.println("응답 : " + data),
                error -> System.out.println("에러 : " + error),
                () -> System.out.println("데이터 없음")
        );
        // 4. doOnNext()
        Mono<String> nextMono = Mono.just("hello")
                .doOnNext(data -> System.out.println(data + " world!"));
        nextMono.subscribe(data -> System.out.println("응답 : " + data));
        // 5. retry()
        Mono<Object> errorMono = Mono.just("I will try .retry() and Mono Exception Test")
                .doOnNext(data -> System.out.println(data + " - OK"))
                .flatMap(data -> Mono.error(new RuntimeException("모노 런타임 예외 발생")))
                .retry(3);
        errorMono.subscribe(
                data -> System.out.println("응답: " + data),
                error -> System.out.println("에러 발생: " + error.getMessage())
        );
    }

    @Test
    public void 요청_로그_테스트() {
        // 5. retry()
        Mono<Object> errorMono = Mono.just("로깅 테스트")
                .flatMap(data -> Mono.error(new RuntimeException("모노 런타임 예외 발생")))
                .doOnError(data -> System.out.println("실패!!!!"))
                .flatMap(data -> Mono.error(new RuntimeException("모노 런타임 예외 발생")))
                .retry(3);
        errorMono.subscribe(
                data -> System.out.println("응답: " + data),
                error -> System.out.println("에러 발생: " + error.getMessage())
        );
    }
}
