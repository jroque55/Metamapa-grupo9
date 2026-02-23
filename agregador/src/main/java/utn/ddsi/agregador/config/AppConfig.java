package utn.ddsi.agregador.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


@Configuration
public class AppConfig {

    // Bean unificado de RestTemplate para hacer peticiones HTTP
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.build();

        // Configurar ObjectMapper con soporte para Java 8 Date/Time (LocalDate, etc.)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);

        // Reemplazar el converter por defecto con el configurado
        restTemplate.getMessageConverters().removeIf(c -> c instanceof MappingJackson2HttpMessageConverter);
        restTemplate.getMessageConverters().add(converter);

        return restTemplate;
    }
}

//    @Bean
//    public OpenTelemetryAppender otelLogAppender(OpenTelemetry openTelemetry) {
//        OpenTelemetryAppender appender = new OpenTelemetryAppender();
//        appender.install(openTelemetry); // Aquí es donde ocurre la magia de unión
//        return appender;
//    }
