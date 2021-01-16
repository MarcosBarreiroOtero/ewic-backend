package es.ewic.backend;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = { HibernateJpaAutoConfiguration.class })
@EnableScheduling
public class AppApplication {

	public static void main(String[] args) {
		// Set java app to UTC
		TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));

		System.setProperty("server.error.include-message", "always");
		System.setProperty("server.servlet.context-path", "/ewic");
		SpringApplication.run(AppApplication.class, args);

	}

}