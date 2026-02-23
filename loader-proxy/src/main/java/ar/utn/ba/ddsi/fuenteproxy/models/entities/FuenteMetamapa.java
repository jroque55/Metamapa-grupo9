package ar.utn.ba.ddsi.fuenteproxy.models.entities;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ar.utn.ba.ddsi.fuenteproxy.models.dtos.HechoInputDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
public class FuenteMetamapa extends FuenteProxy {

    private final WebClient webClient;

    public FuenteMetamapa(@Value("www") String url, WebClient.Builder builder) {
        this.webClient = builder.build();
        this.url = url;
    }

    @Override
    public List<Hecho> obtenerHechos() {
        System.out.println("obtenerHechos en Metamapa");
        List<HechoInputDTO> hechosDTO = obtenerHechosDesdeUrl(this.url + "/hechos");
        List<Hecho> hechos = new ArrayList<>();
        for (HechoInputDTO dto : hechosDTO) {
            Hecho hecho = new Hecho(dto);
            hechos.add(hecho);
        }
        return hechos;
    }

    public List<HechoInputDTO> obtenerHechosDesdeUrl(String urlCompleta) {
        System.out.println("Obteniendo hechos desde URL: " + urlCompleta);
        try {
            return (List<HechoInputDTO>) webClient.get()
                    .uri(urlCompleta)
                    .exchangeToMono(response -> {

                        System.out.println("Status recibido: " + response.statusCode());
                        System.out.println("Content-Type: " + response.headers().contentType());

                        return response.bodyToMono(String.class)  // leemos el JSON crudo
                                .doOnNext(body -> System.out.println("JSON recibido RAW:\n" + body))
                                .flatMap(raw -> {

                                    try {
                                        ObjectMapper mapper = new ObjectMapper();
                                        List<HechoInputDTO> lista =
                                                mapper.readValue(raw, new TypeReference<List<HechoInputDTO>>() {});
                                        return Mono.just(lista);

                                    } catch (Exception ex) {
                                        System.err.println("❌ Error parseando JSON: " + ex.getMessage());
                                        ex.printStackTrace(); // <<--- Esto es lo que te va a mostrar el error REAL
                                        return Mono.just(Collections.emptyList());
                                    }
                                });
                    })
                    .timeout(Duration.ofSeconds(50))
                    .doOnError(ex -> {
                        System.err.println("❌ Error general: " + ex.getMessage());
                        ex.printStackTrace();
                    })
                    .onErrorReturn(Collections.emptyList()).block();

        } catch (Exception ex) {
            System.err.println("❌ Error inesperado: " + ex.getMessage());
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Obtener hechos por colección
    //public List<Hecho> obtenerHechosDeColeccion(String identificador) {
    //    return obtenerHechosDesdeUrl(url + "/colecciones/" + identificador + "/hechos");
    //}

}