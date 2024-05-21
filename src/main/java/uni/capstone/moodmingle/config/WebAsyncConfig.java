package uni.capstone.moodmingle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import uni.capstone.moodmingle.exception.AsyncTaskException;
import uni.capstone.moodmingle.exception.code.ErrorCode;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;

/**
 * 비동기 작업을 위한 ThreadPoolTaskExecutor 설정 Config 클래스
 *
 * @author ijin
 */
@EnableAsync
@Configuration
public class WebAsyncConfig {

    /**
     * 로컬 환경에서의 프로파일 ThreadPoolTaskExecutor 설정
     *
     * @return
     */
    @Bean(name = "AsyncExecutor")
    @Profile("local")
    public Executor asyncExecutor1() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("Async-Thread");
        executor.setRejectedExecutionHandler(rejectedExecutionHandler());
        executor.initialize();
        return executor;
    }

    /**
     * 배포 환경에서의 프로파일 ThreadPoolTaskExecutor 설정
     *
     * @return
     */
    @Bean(name = "AsyncExecutor")
    @Profile("dev | prod")
    public Executor asyncExecutor2() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("Async-Thread");
        executor.setRejectedExecutionHandler(rejectedExecutionHandler());
        executor.initialize();
        return executor;
    }

    private RejectedExecutionHandler rejectedExecutionHandler() {
        return (runnable, executor) -> {
            throw new AsyncTaskException(ErrorCode.FAILED_ASYNC_TASKING);
        };
    }
}
