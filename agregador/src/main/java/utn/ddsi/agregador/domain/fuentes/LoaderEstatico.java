package utn.ddsi.agregador.domain.fuentes;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import utn.ddsi.agregador.adapter.HechoAdapter;
import utn.ddsi.agregador.domain.hecho.Hecho;
import utn.ddsi.agregador.dto.HechoFuenteEstaticaDTO;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LoaderEstatico extends Loader {

    private final RestTemplate restTemplate;


    public LoaderEstatico(@Value("${fuente.estatica.url}") String rutaUrl, RestTemplate restTemplate, HechoAdapter adapter) throws MalformedURLException {
        this.setRuta(rutaUrl);
        this.restTemplate = restTemplate;
        this.setAdapter(adapter);
    }


    public List<Hecho> obtenerHechos() {
        String fuenteUrl = "";
        String urlCarga = this.getRuta() + "/hechos";

        log.info("Iniciando carga estática desde: {}", urlCarga);

        try {
            ResponseEntity<HechoFuenteEstaticaDTO[][]> response =
                    restTemplate.exchange(
                            urlCarga,
                            HttpMethod.GET,
                            null,
                            HechoFuenteEstaticaDTO[][].class
                    );

            HechoFuenteEstaticaDTO[][] hechosDTO = response.getBody();

            if (hechosDTO == null || hechosDTO.length == 0) {
                log.warn("La respuesta de la fuente estática está vacía.");
                return Collections.emptyList();
            }

            log.info("Se recibieron {} HechosDTO para procesar", hechosDTO.length);

            List<Hecho> hechosTransformados = new ArrayList<>();
            for (int i = 0; i < hechosDTO.length; i++) {

                    fuenteUrl = hechosDTO[i][0].getFuente().getRuta();
                    log.debug("Procesando grupo {} - Fuente: {}", i, fuenteUrl);

                    List<Hecho> transformado = this.getAdapter().adaptarHechosDeFuenteEstatica(fuenteUrl, List.of(hechosDTO[i]));
                    hechosTransformados.addAll(transformado);
            }

            log.info("Proceso completado. Total de hechos transformados: {}", hechosTransformados.size());
            return hechosTransformados;

        } catch (Exception e) {
            log.error("Error durante el procesamiento en la URL: {}. Detalle: {}", fuenteUrl, e.getMessage());

            try {
                log.info("Intentando reprocesar fuente fallida: {}", fuenteUrl);
                restTemplate.exchange(
                        this.getRuta() + "/reprocesar/" + fuenteUrl,
                        HttpMethod.POST,
                        null,
                        String.class
                );
                log.info("Solicitud de reprocesamiento enviada correctamente para: {}", fuenteUrl);
            } catch (Exception re) {
                log.error("No se pudo enviar la solicitud de reprocesamiento para {}: {}", fuenteUrl, re.getMessage());
            }

            throw new RuntimeException("Error al obtener hechos desde " + this.getRuta(), e);
        }
    }
}