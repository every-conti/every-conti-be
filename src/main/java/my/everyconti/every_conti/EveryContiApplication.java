package my.everyconti.every_conti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EveryContiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EveryContiApplication.class, args);
	}

}
