package utn.ddsi.agregador.monitoring.healthindicators;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.time.Duration;

@Slf4j
@Component("fuenteDinamica")
public class FuenteDinamicaHealthIndicator extends AbstractDependencyHealthIndicator {

    private final RestTemplate restTemplate;
    private final String healthUrl;

    public FuenteDinamicaHealthIndicator(
            RestTemplateBuilder builder,
            @Value("${fuente.dinamica.url}") String url) {

        this.restTemplate = builder
                .setConnectTimeout(Duration.ofSeconds(2))
                .setReadTimeout(Duration.ofSeconds(3))
                .build();
        this.healthUrl = url + "/actuator/health";
    }

    @Override
    protected String dependencyName() {
        return "fuenteDinamica";
    }

    @Override
    protected String downMessage() {
        return "Fuente dinamica no disponible";
    }

    @Override
    public boolean estaDisponible() {
        try {
            ResponseEntity<String> response =
                    restTemplate.getForEntity(healthUrl, String.class);

            boolean ok = response.getStatusCode().is2xxSuccessful();
            if (!ok) {
                log.warn("FuenteDinamica respondió con código {} para URL {}", response.getStatusCode(), healthUrl);
            }else{
                log.info("FuenteDinamica respondió correctamente con código {} para URL {}", response.getStatusCode(), healthUrl);
            }
            return ok;

        } catch (RestClientException ex) {
            log.error("Error al verificar FuenteDinamica ({}): {}", healthUrl, ex.getMessage());
            return false;
        } catch (Exception ex) {
            log.error("Excepción inesperada al verificar FuenteDinamica: {}", ex.getMessage(), ex);
            return false;
        }
    }
}
