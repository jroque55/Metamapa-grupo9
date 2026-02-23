package ar.utn.ba.ddsi.apipublica.services;

import ar.utn.ba.ddsi.apipublica.models.dtos.HechoFilterDTO;
import ar.utn.ba.ddsi.apipublica.models.dtos.HechoOutputDTO;
import ar.utn.ba.ddsi.apipublica.models.entities.Hecho;
import ar.utn.ba.ddsi.apipublica.models.repository.AdjuntoRepository;
import ar.utn.ba.ddsi.apipublica.models.repository.CategoriaRepository;
import ar.utn.ba.ddsi.apipublica.models.repository.FuenteRepository;
import ar.utn.ba.ddsi.apipublica.models.repository.HechoRepository;
import ar.utn.ba.ddsi.apipublica.models.repository.UbicacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HechoServiceTest {

    @Mock
    private HechoRepository hechoRepository;
    @Mock
    private CategoriaRepository categoriaRepository;
    @Mock
    private FuenteRepository fuenteRepository;
    @Mock
    private UbicacionRepository ubicacionRepository;
    @Mock
    private AdjuntoRepository adjuntoRepository;

    @InjectMocks
    private HechoService hechoService;

    // verifica delegacion de filtros normalizados al repositorio
    @Test
    void buscarConFiltroDelegatesParsedValuesToRepository() {
        HechoFilterDTO filter = new HechoFilterDTO(
                "  Seguridad  ",
                "2024-01-01",
                "2024-01-31",
                "2023-12-01",
                "2023-12-31",
                null,
                "  fraude  ",
                "TIPO"
        );

        // Mock: el repositorio devuelve una página de Hecho; el servicio la convertirá a DTOs
        List<Hecho> expected = List.of(new Hecho());
        lenient().when(hechoRepository.buscarHechosSegun(
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any(Pageable.class)
        )).thenReturn(new PageImpl<>(expected));

        Page<HechoOutputDTO> resultPage = hechoService.buscarConFiltro(filter, 0, 10);
        List<HechoOutputDTO> result = resultPage.getContent();

        // ahora el servicio devuelve DTOs, así que comprobamos tamaño y tipo
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof HechoOutputDTO);

        // Verificar que el repositorio fue llamado con los valores parseados correctos
        verify(hechoRepository).buscarHechosSegun(
                eq("Seguridad"),
                eq(LocalDate.parse("2024-01-01")),
                eq(LocalDate.parse("2024-01-31")),
                eq(LocalDate.parse("2023-12-01")),
                eq(LocalDate.parse("2023-12-31")),
                isNull(),               // provincia en este caso null
                isNull(),              // delta por defecto es null porque no se pasaron coordenadas
                eq("fraude"),
                eq("TIPO"),
                any(Pageable.class)
        );
    }

    // confirma que sin filtros consulta todos los hechos
    @Test
    void buscarConFiltroSinParametrosRecuperaTodo() {
        when(hechoRepository.traerHechosNORechazados(any(Pageable.class))).thenReturn(Page.empty());

        Page<HechoOutputDTO> resultPage = hechoService.buscarConFiltro(null, 0, 10);

        assertNotNull(resultPage);
        assertTrue(resultPage.isEmpty());
        verify(hechoRepository, times(1)).traerHechosNORechazados(any(Pageable.class));
    }

    // asegura que fechas invalidas lanzan error antes de ir al repositorio
    @Test
    void buscarConFiltroLanzaExcepcionPorFechaInvalida() {
        HechoFilterDTO filter = new HechoFilterDTO(
                null,
                "2024-13-01", // mes inválido
                null,
                null,
                null,
                null,
                null,
                null
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> hechoService.buscarConFiltro(filter, 0, 10));
        assertTrue(ex.getMessage().toLowerCase().contains("formato") || ex.getMessage().toLowerCase().contains("fecha"));

        // Asegurarnos que no se llamó al repo con la firma paginada
        verify(hechoRepository, times(0)).buscarHechosSegun(
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any(Pageable.class)
        );
    }
}