package es.ewic.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = { HibernateJpaAutoConfiguration.class })
public class AppApplication {

	public static void main(String[] args) {
		System.setProperty("server.error.include-message", "always");
		System.setProperty("server.servlet.context-path", "/ewic");
		SpringApplication.run(AppApplication.class, args);
	}

}