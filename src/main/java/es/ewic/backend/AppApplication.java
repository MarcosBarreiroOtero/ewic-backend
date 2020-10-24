package es.ewic.backend;

import java.util.TimeZone;
import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication(exclude = { HibernateJpaAutoConfiguration.class })
@EnableAsync
public class AppApplication {

	public static void main(String[] args) {
		// Set java app to UTC
		TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));

		System.setProperty("server.error.include-message", "always");
		System.setProperty("server.servlet.context-path", "/ewic");
		SpringApplication.run(AppApplication.class, args);

	}

	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(2);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("Ewic-");
		executor.initialize();
		return executor;
	}

}