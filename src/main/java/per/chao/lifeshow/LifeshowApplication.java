package per.chao.lifeshow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LifeshowApplication {

	public static void main(String[] args) {
		SpringApplication.run(LifeshowApplication.class, args);
	}

}
