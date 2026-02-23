package ar.utn.ba.ddsi.fuenteproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


//Ver problema de nombres Repositories

@SpringBootApplication
@EntityScan(basePackages = "ar.utn.ba.ddsi.fuenteproxy.models.entities")
@EnableJpaRepositories(basePackages = "ar.utn.ba.ddsi.fuenteproxy.models.repository")
public class LoaderProxyApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoaderProxyApplication.class, args);
        System.out.println("Funciona");
    }
}
