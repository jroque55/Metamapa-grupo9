package ar.utn.ba.ddsi.apipublica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@SpringBootApplication
@EnableScheduling
public class ApiPublicaApplication {

    //Modificar todos los localdate a Localdate time
    public static void main(String[] args) {
        SpringApplication.run(ApiPublicaApplication.class, args);
        System.out.println("API PUBLICA INICIADA");
    }
}