package ar.utn.ba.ddsi.fuenteproxy.Service;

import ar.utn.ba.ddsi.fuenteproxy.models.dtos.HechoOutputDTO;
import ar.utn.ba.ddsi.fuenteproxy.models.entities.*;
import ar.utn.ba.ddsi.fuenteproxy.models.repository.IFuenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HechoServicesTest {

    @Mock
    private IFuenteRepository fuenteRepository;

    @InjectMocks
    private HechoServices hechoServices;

    private Fuente fuenteMetamapa;

    @BeforeEach
    void setUp() {
        fuenteMetamapa = new Fuente(1L, "Metamapa", "http://localhost:8090/export", EnumTipoFuente.METAMAPA);
    }

    @Test
    void buscarHechos_sinFuentesNuevas_retornaListaVacia() {
        // Arrange
        when(fuenteRepository.findByIdFuenteGreaterThan(0L)).thenReturn(Collections.emptyList());

        // Act
        List<HechoOutputDTO> resultado = hechoServices.BuscarHechos();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(fuenteRepository).findByIdFuenteGreaterThan(0L);
    }

    @Test
    void obtenerHechosDeFuente_conFuenteProxy_asignaFuenteCorrectamente() {
        // Arrange
        FuenteProxy fuenteProxyMock = mock(FuenteProxy.class);
        when(fuenteProxyMock.getUrl()).thenReturn("http://localhost:8090/export");
        
        Hecho hecho = crearHecho("Titulo Test", "Descripcion Test");
        when(fuenteProxyMock.obtenerHechos()).thenReturn(List.of(hecho));
        when(fuenteRepository.findByUrl("http://localhost:8090/export")).thenReturn(fuenteMetamapa);

        // Act
        List<Hecho> resultado = hechoServices.ObtenerHechosDeFuente(fuenteProxyMock);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(fuenteMetamapa, resultado.get(0).getFuente());
    }

    @Test
    void obtenerHechosDeFuente_conMultiplesHechos_retornaTodos() {
        // Arrange
        FuenteProxy fuenteProxyMock = mock(FuenteProxy.class);
        when(fuenteProxyMock.getUrl()).thenReturn("http://test.com");
        
        Hecho hecho1 = crearHecho("Titulo 1", "Descripcion 1");
        Hecho hecho2 = crearHecho("Titulo 2", "Descripcion 2");
        Hecho hecho3 = crearHecho("Titulo 3", "Descripcion 3");
        
        when(fuenteProxyMock.obtenerHechos()).thenReturn(Arrays.asList(hecho1, hecho2, hecho3));
        when(fuenteRepository.findByUrl("http://test.com")).thenReturn(fuenteMetamapa);

        // Act
        List<Hecho> resultado = hechoServices.ObtenerHechosDeFuente(fuenteProxyMock);

        // Assert
        assertEquals(3, resultado.size());
        resultado.forEach(h -> assertEquals(fuenteMetamapa, h.getFuente()));
    }

    @Test
    void pasarAHechosOutputDTO_convierteCorrectamente() {
        // Arrange
        Hecho hecho = crearHechoCompleto();
        hecho.setFuente(fuenteMetamapa);

        // Act
        List<HechoOutputDTO> resultado = hechoServices.pasarAHechosOuputDTO(List.of(hecho));

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        
        HechoOutputDTO dto = resultado.get(0);
        assertEquals("Titulo Test", dto.getTitulo());
        assertEquals("Descripcion Test", dto.getDescripcion());
        assertEquals("Categoria Test", dto.getCategoria());
    }

    @Test
    void pasarAHechosOutputDTO_conListaVacia_retornaListaVacia() {
        // Act
        List<HechoOutputDTO> resultado = hechoServices.pasarAHechosOuputDTO(Collections.emptyList());

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarNuevasFuentes_conFuenteMetamapa_agregaFuenteProxy() {
        // Arrange
        when(fuenteRepository.findByIdFuenteGreaterThan(0L)).thenReturn(List.of(fuenteMetamapa));

        // Act
        hechoServices.BuscarNuevasFuentes();

        // Assert
        verify(fuenteRepository).findByIdFuenteGreaterThan(0L);
    }

    @Test
    void buscarNuevasFuentes_conFuenteEstatica_noAgregaFuenteProxy() {
        // Arrange
        Fuente fuenteEstatica = new Fuente(2L, "Estatica", "http://estatica.com", EnumTipoFuente.ESTATICA);
        when(fuenteRepository.findByIdFuenteGreaterThan(0L)).thenReturn(List.of(fuenteEstatica));

        // Act - no debería lanzar excepción
        assertDoesNotThrow(() -> hechoServices.BuscarNuevasFuentes());

        verify(fuenteRepository).findByIdFuenteGreaterThan(0L);
    }

    private Hecho crearHecho(String titulo, String descripcion) {
        Categoria categoria = new Categoria("Categoria Test");
        Ubicacion ubicacion = new Ubicacion(-34.6f, -58.4f);
        Hecho hecho = new Hecho(titulo, descripcion, categoria, ubicacion, LocalDate.of(2025, 6, 15));
        hecho.setFechaDeCarga(LocalDate.now());
        hecho.setTipoHecho(EnumTipoHecho.TEXTO);
        return hecho;
    }

    private Hecho crearHechoCompleto() {
        Hecho hecho = crearHecho("Titulo Test", "Descripcion Test");
        hecho.setTipoHecho(EnumTipoHecho.TEXTO);
        return hecho;
    }
}
