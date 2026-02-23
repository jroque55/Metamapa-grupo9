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
import utn.ddsi.agregador.dto.HechoFuenteProxyDTO;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LoaderProxy extends Loader {

    private final RestTemplate restTemplate;  //Por ahí se puede pasar a Loader

    //ESta bien que este harcodeado porque debería solo apuntar a una LoaderProxy
    public LoaderProxy(@Value("${fuente.proxy.url}") String rutaUrl, RestTemplate restTemplate, HechoAdapter adapter) throws MalformedURLException {
        setRuta(rutaUrl);
        this.restTemplate = restTemplate;
        setAdapter(adapter);
    }

    @Override
    public List<Hecho> obtenerHechos() {
        String urlCarga = getRuta() + "/hechos";
        log.info("LoaderProxy: Iniciando recuperación de hechos desde el proxy: {}", urlCarga);

        try {
            ResponseEntity<HechoFuenteProxyDTO[]> response =
                    restTemplate.exchange(
                            urlCarga,
                            HttpMethod.GET,
                            null,
                            HechoFuenteProxyDTO[].class
                    );

            log.info("LoaderProxy: Respuesta recibida. Status Code: {}", response.getStatusCode());

            HechoFuenteProxyDTO[] hechosDTO = response.getBody();

            if (hechosDTO == null || hechosDTO.length == 0) {
                log.warn("LoaderProxy: El cuerpo de la respuesta del proxy está vacío o es nulo.");
                return Collections.emptyList();
            }

            log.info("LoaderProxy: Se recuperaron {} HechosDTO del proxy. Iniciando transformación...", hechosDTO.length);

            List<Hecho> hechosTransformados = new ArrayList<>();
            for (int i = 0; i < hechosDTO.length; i++) {
                log.debug("LoaderProxy: Transformando elemento {} de {}", i + 1, hechosDTO.length);
                List<Hecho> transformado = this.getAdapter().adaptarHechosDeFuenteProxy(List.of(hechosDTO[i]));
                hechosTransformados.addAll(transformado);
            }

            log.info("LoaderProxy: Proceso finalizado. Total de hechos transformados: {}", hechosTransformados.size());
            return hechosTransformados;

        } catch (Exception e) {
            log.error("LoaderProxy: ERROR CRÍTICO al conectar con el proxy en {}. Detalle: {}", urlCarga, e.getMessage());
            throw new RuntimeException("Error al obtener hechos desde " + getRuta(), e);
        }
    }
}