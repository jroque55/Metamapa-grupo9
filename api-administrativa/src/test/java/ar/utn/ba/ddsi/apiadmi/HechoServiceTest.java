package ar.utn.ba.ddsi.apiadmi;

import ar.utn.ba.ddsi.apiadmi.models.entities.hecho.Etiqueta;
import ar.utn.ba.ddsi.apiadmi.models.entities.hecho.Hecho;
import ar.utn.ba.ddsi.apiadmi.models.repository.IEtiquetaRepository;
import ar.utn.ba.ddsi.apiadmi.models.repository.IHechosRepository;
import ar.utn.ba.ddsi.apiadmi.servicies.HechoServices;
import ar.utn.ba.ddsi.apiadmi.utils.EnumEstadoHecho;
import ar.utn.ba.ddsi.apiadmi.utils.EnumEstadoSol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class HechoServicesTest {

    private HechoServices hechoServices;
    private IHechosRepository hechoRepo;
    private IEtiquetaRepository etiquetaRepo;

    @BeforeEach
    void setUp() {

            hechoRepo = mock(IHechosRepository.class);
            etiquetaRepo = mock(IEtiquetaRepository.class);

            hechoServices = new HechoServices(hechoRepo, etiquetaRepo);

    }

    // ------------------------------------------------------------
    //  TEST actualizarElEstadoDelHecho
    // ------------------------------------------------------------
    @Test
    void deberiaActualizarEstadoCuandoSolicitudEsAceptada() {

        Hecho hecho = new Hecho();
        hecho.setEstado(EnumEstadoHecho.ALTA);

        hechoServices.actualizarElEstadoDelHecho(hecho, EnumEstadoSol.ACEPTADA);

        assertEquals(EnumEstadoHecho.BAJA, hecho.getEstado());
        verify(hechoRepo).save(hecho);
    }

    @Test
    void noDebeActualizarNiGuardarCuandoNoEsAceptada() {

        Hecho hecho = new Hecho();
        hecho.setEstado(EnumEstadoHecho.ALTA);

        hechoServices.actualizarElEstadoDelHecho(hecho, EnumEstadoSol.RECHAZADA);

        assertEquals(EnumEstadoHecho.ALTA, hecho.getEstado());
        verify(hechoRepo, never()).save(any());
    }


    // ------------------------------------------------------------
    //  TEST actualizarEtiqueta
    // ------------------------------------------------------------
    @Test
    void deberiaAsignarEtiquetaExistente() {

        Long idHecho = 1L;

        Hecho hecho = new Hecho();
        hecho.setId_hecho(idHecho);

        Etiqueta etiquetaExistente = new Etiqueta();
        etiquetaExistente.setNombre("PRUEBA");

        when(hechoRepo.findById(idHecho)).thenReturn(Optional.of(hecho));
        when(etiquetaRepo.findByNombreIgnoreCase("PRUEBA")).thenReturn(Optional.of(etiquetaExistente));

        hechoServices.actualizarEtiqueta(idHecho, "  prueba ");

        assertEquals(etiquetaExistente, hecho.getEtiqueta());

        verify(hechoRepo).save(hecho);
        verify(etiquetaRepo, never()).save(any());
    }


    @Test
    void deberiaCrearNuevaEtiquetaSiNoExiste() {

        Long idHecho = 1L;

        Hecho hecho = new Hecho();
        hecho.setId_hecho(idHecho);

        when(hechoRepo.findById(idHecho)).thenReturn(Optional.of(hecho));
        when(etiquetaRepo.findByNombreIgnoreCase("NUEVA")).thenReturn(Optional.empty());

        Etiqueta nueva = new Etiqueta();
        nueva.setNombre("NUEVA");

        when(etiquetaRepo.save(any(Etiqueta.class))).thenReturn(nueva);

        hechoServices.actualizarEtiqueta(idHecho, " nueva ");

        assertEquals(nueva, hecho.getEtiqueta());

        verify(etiquetaRepo).save(any(Etiqueta.class));
        verify(hechoRepo).save(hecho);
    }
}
