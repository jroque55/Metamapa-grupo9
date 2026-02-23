package utn.ddsi.agregador.domain.fuentes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import utn.ddsi.agregador.adapter.HechoAdapter;
import utn.ddsi.agregador.domain.hecho.Hecho;
import utn.ddsi.agregador.dto.AdjuntoDTO;
import utn.ddsi.agregador.dto.HechoFuenteDinamicaDTO;
import utn.ddsi.agregador.dto.UbicacionDTO;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoaderDinamicoTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private HechoAdapter hechoAdapter;

    private LoaderDinamico loaderDinamico;

    private static final String URL_BASE = "http://localhost:8090";

    @BeforeEach
    void setUp() {
        loaderDinamico = new LoaderDinamico(URL_BASE, restTemplate, hechoAdapter);
    }

    @Test
    void obtenerHechos_conHechosValidos_retornaListaDeHechos() {
        // Arrange
        HechoFuenteDinamicaDTO dto1 = crearHechoDTO("Titulo 1", "Descripcion 1");
        HechoFuenteDinamicaDTO dto2 = crearHechoDTO("Titulo 2", "Descripcion 2");
        HechoFuenteDinamicaDTO[] hechosDTO = {dto1, dto2};

        ResponseEntity<HechoFuenteDinamicaDTO[]> responseEntity = 
            new ResponseEntity<>(hechosDTO, HttpStatus.OK);

        when(restTemplate.exchange(
            eq(URL_BASE + "/export/hechos"),
            eq(HttpMethod.GET),
            isNull(),
            eq(HechoFuenteDinamicaDTO[].class)
        )).thenReturn(responseEntity);

        Hecho hecho1 = new Hecho();
        hecho1.setTitulo("Titulo 1");
        Hecho hecho2 = new Hecho();
        hecho2.setTitulo("Titulo 2");

        when(hechoAdapter.adaptarHechosDeFuenteDinamica(anyList()))
            .thenReturn(Arrays.asList(hecho1, hecho2));

        // Act
        List<Hecho> resultado = loaderDinamico.obtenerHechos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Titulo 1", resultado.get(0).getTitulo());
        assertEquals("Titulo 2", resultado.get(1).getTitulo());

        verify(restTemplate).exchange(
            eq(URL_BASE + "/export/hechos"),
            eq(HttpMethod.GET),
            isNull(),
            eq(HechoFuenteDinamicaDTO[].class)
        );
        verify(hechoAdapter).adaptarHechosDeFuenteDinamica(anyList());
    }

    @Test
    void obtenerHechos_conRespuestaVacia_retornaListaVacia() {
        // Arrange
        ResponseEntity<HechoFuenteDinamicaDTO[]> responseEntity = 
            new ResponseEntity<>(new HechoFuenteDinamicaDTO[0], HttpStatus.OK);

        when(restTemplate.exchange(
            eq(URL_BASE + "/export/hechos"),
            eq(HttpMethod.GET),
            isNull(),
            eq(HechoFuenteDinamicaDTO[].class)
        )).thenReturn(responseEntity);

        // Act
        List<Hecho> resultado = loaderDinamico.obtenerHechos();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(hechoAdapter, never()).adaptarHechosDeFuenteDinamica(anyList());
    }

    @Test
    void obtenerHechos_conRespuestaNula_retornaListaVacia() {
        // Arrange
        ResponseEntity<HechoFuenteDinamicaDTO[]> responseEntity = 
            new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(
            eq(URL_BASE + "/export/hechos"),
            eq(HttpMethod.GET),
            isNull(),
            eq(HechoFuenteDinamicaDTO[].class)
        )).thenReturn(responseEntity);

        // Act
        List<Hecho> resultado = loaderDinamico.obtenerHechos();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(hechoAdapter, never()).adaptarHechosDeFuenteDinamica(anyList());
    }

    @Test
    void obtenerHechos_conErrorDeConexion_lanzaRuntimeException() {
        // Arrange
        when(restTemplate.exchange(
            eq(URL_BASE + "/export/hechos"),
            eq(HttpMethod.GET),
            isNull(),
            eq(HechoFuenteDinamicaDTO[].class)
        )).thenThrow(new RuntimeException("Connection refused"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> loaderDinamico.obtenerHechos());
        
        assertTrue(exception.getMessage().contains("Error al obtener hechos desde"));
    }

    @Test
    void obtenerHechos_conHechoConAdjuntos_procesamientoCorrectoDeLaLista() {
        // Arrange
        HechoFuenteDinamicaDTO dto = new HechoFuenteDinamicaDTO();
        dto.setTitulo("Hecho con adjuntos");
        dto.setDescripcion("Descripcion");
        dto.setFecha(LocalDate.of(2025, 1, 15));
        dto.setCategoria("Categoria Test");
        dto.setTipoDeHecho("MULTIMEDIA");
        
        AdjuntoDTO adjunto1 = new AdjuntoDTO(1L, "http://url1.com/img.jpg", "IMAGEN");
        AdjuntoDTO adjunto2 = new AdjuntoDTO(2L, "http://url2.com/video.mp4", "VIDEO");
        dto.setAdjuntos(Arrays.asList(adjunto1, adjunto2));

        HechoFuenteDinamicaDTO[] hechosDTO = {dto};
        ResponseEntity<HechoFuenteDinamicaDTO[]> responseEntity = 
            new ResponseEntity<>(hechosDTO, HttpStatus.OK);

        when(restTemplate.exchange(
            anyString(),
            eq(HttpMethod.GET),
            isNull(),
            eq(HechoFuenteDinamicaDTO[].class)
        )).thenReturn(responseEntity);

        Hecho hechoAdaptado = new Hecho();
        hechoAdaptado.setTitulo("Hecho con adjuntos");
        when(hechoAdapter.adaptarHechosDeFuenteDinamica(anyList()))
            .thenReturn(List.of(hechoAdaptado));

        // Act
        List<Hecho> resultado = loaderDinamico.obtenerHechos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        
        // Verificar que se pasó la lista con adjuntos al adapter
        verify(hechoAdapter).adaptarHechosDeFuenteDinamica(argThat(list -> {
            HechoFuenteDinamicaDTO dtoRecibido = list.get(0);
            return dtoRecibido.getAdjuntos() != null && 
                   dtoRecibido.getAdjuntos().size() == 2 &&
                   "MULTIMEDIA".equals(dtoRecibido.getTipoDeHecho());
        }));
    }

    private HechoFuenteDinamicaDTO crearHechoDTO(String titulo, String descripcion) {
        HechoFuenteDinamicaDTO dto = new HechoFuenteDinamicaDTO();
        dto.setTitulo(titulo);
        dto.setDescripcion(descripcion);
        dto.setFecha(LocalDate.of(2025, 6, 15));
        dto.setUbicacion(new UbicacionDTO(-34.6f, -58.4f));
        dto.setCategoria("Categoria Test");
        dto.setTipoDeHecho("TEXTO");
        return dto;
    }
}
