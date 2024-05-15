package uni.capstone.moodmingle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableCaching
@EnableFeignClients
@EnableJpaAuditing
@SpringBootApplication
public class MoodMingleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoodMingleApplication.class, args);
	}

}
