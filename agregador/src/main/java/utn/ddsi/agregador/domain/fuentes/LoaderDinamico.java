package utn.ddsi.agregador.domain.fuentes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import utn.ddsi.agregador.adapter.HechoAdapter;
import utn.ddsi.agregador.domain.hecho.Hecho;
import utn.ddsi.agregador.dto.HechoFuenteDinamicaDTO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class LoaderDinamico extends Loader {
    private final RestTemplate restTemplate;

    public LoaderDinamico(@Value("${fuente.dinamica.url}") String rutaUrl, RestTemplate restTemplate, HechoAdapter adapter) {
        this.setRuta(rutaUrl);
        this.restTemplate = restTemplate;
        this.setAdapter(adapter);
    }
    @Override
    public List<Hecho> obtenerHechos() {
        String urlCompleta = getRuta() + "/export/hechos";
        log.info("Iniciando petición GET a fuente externa: {}", urlCompleta);

        try {
            ResponseEntity<HechoFuenteDinamicaDTO[]> response =
                    restTemplate.exchange(
                            urlCompleta,
                            HttpMethod.GET,
                            null,
                            HechoFuenteDinamicaDTO[].class
                    );

            log.info("Respuesta recibida de la API externa. Status Code: {}", response.getStatusCode());

            HechoFuenteDinamicaDTO[] hechosDTO = response.getBody();

            if (hechosDTO == null || hechosDTO.length == 0) {
                log.warn("La API externa en {} devolvió una lista vacía o nula", urlCompleta);
                return Collections.emptyList();
            }

            log.debug("Se recuperaron {} HechosDTOs de la fuente externa. Iniciando adaptación...", hechosDTO.length);

            List<Hecho> hechosAdaptados = this.getAdapter().adaptarHechosDeFuenteDinamica(Arrays.asList(hechosDTO));

            log.info("Proceso de carga dinámica finalizado. Hechos adaptados con éxito: {}", hechosAdaptados.size());
            return hechosAdaptados;

        } catch (Exception e) {
            log.error("ERROR CRÍTICO: Falló la conexión o el procesamiento de la fuente externa en {}", urlCompleta);
            log.error("Detalle del error: {}", e.getMessage());
            throw new RuntimeException("Error al obtener hechos desde " + getRuta(), e);
        }
    }
}