package utn.ddsi.agregador.domain.agregador;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utn.ddsi.agregador.domain.solicitudEliminacion.DetectorDeSpam;
import utn.ddsi.agregador.domain.solicitudEliminacion.GestorDeSolicitudes;
import utn.ddsi.agregador.domain.solicitudEliminacion.SolicitudEliminacion;
import utn.ddsi.agregador.repository.IRepositorySolicitudes;
import utn.ddsi.agregador.utils.EnumEstadoSol;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestorDeSolicitudesTest {

    @Mock
    IRepositorySolicitudes repository;

    @Mock
    DetectorDeSpam detector;

    @InjectMocks
    GestorDeSolicitudes gestor;

    @Test
    void cuandoElDetectorDiceQueNoEsSpam_estadoPendiente() {
        SolicitudEliminacion s = new SolicitudEliminacion(
                null,
                LocalDate.now(),
                "motivo de prueba la verdad"
        );

        when(detector.esSpam(anyString())).thenReturn(false);

        gestor.procesarSolicitud(s);

        assertEquals(EnumEstadoSol.PENDIENTE, s.getEstado());
        verify(repository).save(s);
    }

    @Test
    void cuandoElDetectorDiceQueEsSpam_estadoRechazada() {
        SolicitudEliminacion s = new SolicitudEliminacion(
                null,
                LocalDate.now(),
                "bitcoin gratis a 5KM!!!"
        );

        when(detector.esSpam(anyString())).thenReturn(true);

        gestor.procesarSolicitud(s);

        assertEquals(EnumEstadoSol.RECHAZADA, s.getEstado());
        verify(repository).save(s);
    }
    @Test
    void procesaTodasLasSolicitudes() {

        // Solicitudes
        SolicitudEliminacion s1 = new SolicitudEliminacion(
                null,
                LocalDate.now(),
                "spam spam"
        );

        SolicitudEliminacion s2 = new SolicitudEliminacion(
                null,
                LocalDate.now(),
                "motivo valido"
        );

        // Mock del repositorio
        when(repository.findAll()).thenReturn(List.of(s1, s2));

        // Mock del detector: s1 es spam, s2 no
        when(detector.esSpam("spam spam")).thenReturn(true);
        when(detector.esSpam("motivo valido")).thenReturn(false);

        // Ejecutar
        gestor.procesarTodasLasSolicitudes();

        // Verificaciones
        verify(repository).findAll();
        verify(detector).esSpam("spam spam");
        verify(detector).esSpam("motivo valido");
        verify(repository, times(2)).save(any(SolicitudEliminacion.class));

        assertEquals(EnumEstadoSol.RECHAZADA, s1.getEstado());
        assertEquals(EnumEstadoSol.PENDIENTE, s2.getEstado());
    }
}