package ar.utn.ba.ddsi.apipublica.services;

import ar.utn.ba.ddsi.apipublica.exception.RecursoNoEncontradoException;
import ar.utn.ba.ddsi.apipublica.models.dtos.SolicitudCreateDTO;
import ar.utn.ba.ddsi.apipublica.models.dtos.SolicitudOutputDTO;
import ar.utn.ba.ddsi.apipublica.models.entities.Hecho;
import ar.utn.ba.ddsi.apipublica.models.entities.SolicitudEliminacion;
import ar.utn.ba.ddsi.apipublica.models.repository.HechoRepository;
import ar.utn.ba.ddsi.apipublica.models.repository.SolicitudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SolicitudServiceTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private HechoRepository hechoRepository;

    @InjectMocks
    private SolicitudService solicitudService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearSolicitud_cuandoDatosValidos_debeGuardarYRetornarSolicitud() {
        // Arrange
        SolicitudCreateDTO dto = new SolicitudCreateDTO();
        dto.setIdHecho(10L);
        dto.setIdContribuyente(5L);
        dto.setMotivo("Información incorrecta");

        Hecho hecho = new Hecho();

        SolicitudEliminacion solicitudGuardada = new SolicitudEliminacion(
                5L, hecho, LocalDate.now(), "Información incorrecta"
        );
        solicitudGuardada.setId_solicitud(1L);

        when(hechoRepository.findById(10L)).thenReturn(Optional.of(hecho));
        when(solicitudRepository.save(any(SolicitudEliminacion.class)))
                .thenReturn(solicitudGuardada);

        // Act
        SolicitudEliminacion resultado = solicitudService.crearSolicitud(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId_solicitud());
        assertEquals("Información incorrecta", resultado.getMotivo());

        verify(hechoRepository).findById(10L);
        verify(solicitudRepository).save(any(SolicitudEliminacion.class));
    }

    @Test
    void listarSolicitudes_cuandoNoHay_debeRetornarListaVacia() {
        when(solicitudRepository.findAll()).thenReturn(Collections.emptyList());

        List<SolicitudOutputDTO> resultado = solicitudService.listarSolicitudes();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(solicitudRepository).findAll();
    }

    @Test
    void listarSolicitudes_debeRetornarTodosComoDTO() {
        // Arrange
        Hecho hecho = new Hecho();
        SolicitudEliminacion s1 = new SolicitudEliminacion(1L, hecho, LocalDate.now(), "Motivo 1");
        s1.setId_solicitud(1L);
        SolicitudEliminacion s2 = new SolicitudEliminacion(2L, hecho, LocalDate.now(), "Motivo 2");
        s2.setId_solicitud(2L);

        when(solicitudRepository.findAll()).thenReturn(List.of(s1, s2));

        // Act
        List<SolicitudOutputDTO> resultado = solicitudService.listarSolicitudes();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(solicitudRepository).findAll();
    }

    @Test
    void obtenerSolicitudPorId_cuandoExiste_debeRetornarDTO() {
        // Arrange
        Hecho hecho = new Hecho();
        SolicitudEliminacion solicitud = new SolicitudEliminacion(1L, hecho, LocalDate.now(), "Motivo");
        solicitud.setId_solicitud(7L);

        when(solicitudRepository.findById(7L)).thenReturn(Optional.of(solicitud));

        // Act
        SolicitudOutputDTO resultado = solicitudService.obtenerSolicitudPorId(7L);

        // Assert
        assertNotNull(resultado);
        assertEquals(7L, resultado.getId_solicitud());
        verify(solicitudRepository).findById(7L);
    }

    @Test
    void obtenerSolicitudPorId_cuandoNoExiste_debeThrowRecursoNoEncontrado() {
        when(solicitudRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> solicitudService.obtenerSolicitudPorId(99L));

        verify(solicitudRepository).findById(99L);
    }
}