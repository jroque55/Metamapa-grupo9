package ar.utn.ba.ddsi.apipublica.services;

import ar.utn.ba.ddsi.apipublica.models.dtos.HechoFilterDTO;
import ar.utn.ba.ddsi.apipublica.models.dtos.HechoOutputDTO;
import ar.utn.ba.ddsi.apipublica.models.entities.Coleccion;
import ar.utn.ba.ddsi.apipublica.models.entities.Hecho;
import ar.utn.ba.ddsi.apipublica.models.repository.ColeccionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ColeccionServiceTest {

    @Mock
    private ColeccionRepository coleccionRepository;

    @InjectMocks
    private ColeccionService coleccionService;

    /*** 1) Delegación correcta cuando modo es NOCURADO ***/
    @Test
    void buscarHechosSegunDelegatesWhenModoNoCurado() {
        Long coleccionId = 42L;

        when(coleccionRepository.findById(coleccionId))
                .thenReturn(Optional.of(new Coleccion()));

        // Devuelve lista de entidades, luego el service las convierte a DTOs
        lenient().when(coleccionRepository.buscarEnColeccionIrrestricta(
                anyLong(), any(), any(), any(), any(), any(), any(), any(), any(), any()
        )).thenReturn(List.of(new Hecho()));

        HechoFilterDTO filter = new HechoFilterDTO(
                "   Salud",
                "2024-02-01",
                "2024-02-28",
                null,
                null,
                null,
                null,
                "   vacuna  "
        );

        List<HechoOutputDTO> result =
                coleccionService.buscarHechosSegun(filter, "NOCURADO", coleccionId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof HechoOutputDTO);

        verify(coleccionRepository).buscarEnColeccionIrrestricta(
                eq(42L),
                eq("Salud"),
                eq(LocalDate.parse("2024-02-01")),
                eq(LocalDate.parse("2024-02-28")),
                isNull(),
                isNull(),
                isNull(),     // provincia
                isNull(),     // delta
                isNull(),
                eq("vacuna")
        );
    }

    /*** 2) Modo CURADO fuerza consensuado true ***/
    @Test
    void buscarHechosSegunInterpretaModoCurado() {
        Long coleccionId = 7L;

        when(coleccionRepository.findById(coleccionId))
                .thenReturn(Optional.of(new Coleccion()));

        lenient().when(coleccionRepository.buscarEnColeccionSegun(
                anyLong(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()
        )).thenReturn(List.of());

        HechoFilterDTO filter = new HechoFilterDTO();

        coleccionService.buscarHechosSegun(filter, "CURADO", coleccionId);

        ArgumentCaptor<Boolean> curadoCaptor = ArgumentCaptor.forClass(Boolean.class);

        verify(coleccionRepository).buscarEnColeccionSegun(
                eq(7L),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                curadoCaptor.capture(),
                isNull(),
                isNull()
        );

        assertEquals(Boolean.TRUE, curadoCaptor.getValue());
    }

    /*** 3) Modo inválido: se interpreta como irrrestricto (comportamiento actual) ***/
    @Test
    void buscarHechosSegunModoInvalidoSeInterpretaComoIrrestricta() {
        Long coleccionId = 9L;
        when(coleccionRepository.findById(coleccionId))
                .thenReturn(Optional.of(new Coleccion()));

        lenient().when(coleccionRepository.buscarEnColeccionIrrestricta(
                anyLong(), any(), any(), any(), any(), any(), any(), any(), any(), any()
        )).thenReturn(List.of());

        HechoFilterDTO filter = new HechoFilterDTO();

        // No debe lanzar excepción; simplemente se usará la búsqueda irrrestricta
        coleccionService.buscarHechosSegun(filter, "XXXX", coleccionId);

        verify(coleccionRepository).buscarEnColeccionIrrestricta(eq(9L), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull());
    }

    /*** 4) Colección inexistente lanza error ***/
    @Test
    void buscarHechosSegunErrorColeccionNoExiste() {
        when(coleccionRepository.findById(100L)).thenReturn(Optional.empty());

        HechoFilterDTO filter = new HechoFilterDTO();

        assertThrows(IllegalArgumentException.class,
                () -> coleccionService.buscarHechosSegun(filter, null, 100L));
    }

    /*** 5) Devuelve DTOs aunque el repo devuelva lista vacía ***/
    @Test
    void buscarHechosSegunDevuelveListaDTOVacia() {
        Long id = 1L;

        when(coleccionRepository.findById(id))
                .thenReturn(Optional.of(new Coleccion()));

        lenient().when(coleccionRepository.buscarEnColeccionIrrestricta(
                anyLong(), any(), any(), any(), any(), any(), any(), any(), any(), any()
        )).thenReturn(List.of());

        List<HechoOutputDTO> result =
                coleccionService.buscarHechosSegun(new HechoFilterDTO(), null, id);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}