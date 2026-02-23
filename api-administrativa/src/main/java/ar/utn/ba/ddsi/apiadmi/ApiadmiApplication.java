package ar.utn.ba.ddsi.apiadmi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class ApiadmiApplication {

	public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(ApiadmiApplication.class, args);
		System.out.println("Ejecuto api");

	}
}
