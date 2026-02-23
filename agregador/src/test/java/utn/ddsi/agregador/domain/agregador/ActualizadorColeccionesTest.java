package utn.ddsi.agregador.domain.agregador;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import utn.ddsi.agregador.domain.coleccion.Coleccion;
import utn.ddsi.agregador.domain.coleccion.HechoXColeccion;
import utn.ddsi.agregador.domain.condicion.CondicionTitulo;
import utn.ddsi.agregador.domain.condicion.InterfaceCondicion;
import utn.ddsi.agregador.domain.fuentes.Loader;
import utn.ddsi.agregador.domain.hecho.Hecho;
import utn.ddsi.agregador.domain.solicitudEliminacion.GestorDeSolicitudes;
import utn.ddsi.agregador.repository.IRepositoryColecciones;
import utn.ddsi.agregador.repository.IRepositoryFuentes;
import utn.ddsi.agregador.repository.IRepositoryHechoXColeccion;
import utn.ddsi.agregador.repository.IRepositoryHechos;


import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActualizadorColeccionesTest {

    @Mock private IRepositoryColecciones repositoryColecciones;
    @Mock private IRepositoryHechos repositoryHechos;
    @Mock private Normalizador normalizador;
    @Mock private GestorDeSolicitudes gestorSolicitudes;
    @Mock private FiltradorDeHechos filtrador;
    @Mock private Loader loader1;
    @Mock private Loader loader2;
    @Mock private IRepositoryHechoXColeccion repositoryHechoXColeccion;
    @Mock private IRepositoryFuentes repositoryFuentes;

    @InjectMocks
    private ActualizadorColecciones actualizador;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        List<Loader> loaders = List.of(loader1, loader2);
        actualizador = new ActualizadorColecciones(
            repositoryColecciones,
            repositoryHechos,
            normalizador,
            gestorSolicitudes,
            loaders,
            filtrador,
            repositoryHechoXColeccion, repositoryFuentes
        );
    }

    @Test
    void traerHechosDeLoaders_devuelveHechosDeTodosLosLoaders() {

        when(loader1.obtenerHechos()).thenReturn(List.of(new Hecho()));
        when(loader2.obtenerHechos()).thenReturn(List.of(new Hecho(), new Hecho()));

        List<Hecho> result = actualizador.traerHechosDeLoaders();

        assertEquals(3, result.size());
        verify(loader1, times(1)).obtenerHechos();
        verify(loader2, times(1)).obtenerHechos();
    }
    @Test
    void depurarHechos_hechosVacios() {
        Hecho h1 = new Hecho();
        Hecho h2 = new Hecho();
        List<Hecho> hechos = List.of(h1, h2);

        when(loader1.obtenerHechos()).thenReturn(hechos);
        when(loader2.obtenerHechos()).thenReturn(List.of());

        when(normalizador.normalizar(anyList())).thenReturn(hechos);
        List<Hecho> resultado = actualizador.depurarHechos();

        assertEquals(2, resultado.size());
        verify(normalizador).normalizar(anyList());
        verify(repositoryHechos).saveAll(hechos);
    }
    @Test
    void depurarHechos_hechosConDatos() {
        Hecho hechoOriginal = new Hecho("Titulo1", "Desc1", null, null, LocalDate.now(), null);
        Hecho hechoNormalizado = new Hecho("TituloNormal", "Desc", null, null, LocalDate.now(), null);
        when(loader1.obtenerHechos())
                .thenReturn(List.of(hechoOriginal));
        when(normalizador.normalizar(List.of(hechoOriginal)))
                .thenReturn(List.of(hechoNormalizado));
        List<Hecho> resultado = actualizador.depurarHechos();
        verify(repositoryHechos).saveAll(List.of(hechoNormalizado));
        assertEquals(1, resultado.size());
        assertEquals("TituloNormal", resultado.get(0).getTitulo());
    }
    @Test
    void testActualizarColecciones_basico() {
        Hecho h1 = new Hecho();
        when(loader1.obtenerHechos()).thenReturn(List.of(h1));
        when(loader2.obtenerHechos()).thenReturn(Collections.emptyList());
        when(normalizador.normalizar(anyList())).thenReturn(List.of(h1));

        Coleccion coleccion = mock(Coleccion.class);
        when(coleccion.getId_coleccion()).thenReturn(10L);

        when(repositoryColecciones.findAll()).thenReturn(List.of(coleccion));
        when(repositoryHechos.findAll()).thenReturn(List.of(h1));

        when(repositoryHechoXColeccion.findByColeccion(10L)).thenReturn(Collections.emptyList());

        InterfaceCondicion c1 = mock(InterfaceCondicion.class);
        when(repositoryColecciones.findByIdCondiciones(10L)).thenReturn(List.of(c1));

        when(filtrador.devolverHechosAPartirDe(anyList(), anyList()))
                .thenReturn(List.of(h1));

        actualizador.actualizarColecciones();

        verify(repositoryHechoXColeccion, times(1)).save(any(HechoXColeccion.class));

        verify(repositoryColecciones).saveAll(anyList());
    }
    @Test
    void actualizarColecciones_agregaHechoNuevoCuandoNoExisteEnColeccion() {
        // ---------- Arrange ----------

        // Hechos
        Hecho hNuevo = new Hecho("Titulo nuevo", "Desc", null, null,
                LocalDate.now(), null);
        hNuevo.setId_hecho(2L);

        Hecho hExistente = new Hecho("Titulo existente", "Desc", null, null,
                LocalDate.now(), null);
        hExistente.setId_hecho(1L);

        // Loaders
        when(loader1.obtenerHechos()).thenReturn(List.of(hNuevo));
        when(loader2.obtenerHechos()).thenReturn(Collections.emptyList());

        // Normalizador
        when(normalizador.normalizar(anyList())).thenReturn(List.of(hNuevo));

        // Repositorio de hechos
        when(repositoryHechos.findAll()).thenReturn(List.of(hExistente, hNuevo));
        when(repositoryHechos.findFirstByTitulo(anyString())).thenReturn(null);

        // Colección
        Coleccion coleccion = mock(Coleccion.class);
        when(coleccion.getId_coleccion()).thenReturn(100L);
        when(repositoryColecciones.findAll()).thenReturn(List.of(coleccion));

        // Hechos ya presentes en la colección
        HechoXColeccion hxcExistente = new HechoXColeccion(hExistente, coleccion, false);
        when(repositoryHechoXColeccion.findByColeccion(100L))
                .thenReturn(List.of(hxcExistente));

        // Condiciones de pertenencia
        InterfaceCondicion condicion = mock(InterfaceCondicion.class);
        when(repositoryColecciones.findByIdCondiciones(100L))
                .thenReturn(List.of(condicion));

        // Fuentes (no interesan para este test)
        when(repositoryFuentes.findFuentesByColeccion(100L))
                .thenReturn(Collections.emptyList());

        // Filtrador (IMPORTANTE: stubs generales)
        when(filtrador.devolverHechosDeFuentes(anyList(), anyList()))
                .thenReturn(List.of(hNuevo));

        when(filtrador.devolverHechosAPartirDe(anyList(), anyList()))
                .thenReturn(List.of(hNuevo));

        // No existe aún la relación nuevo–colección
        when(repositoryHechoXColeccion.findByConjunto(100L, 2L))
                .thenReturn(null);

        // ---------- Act ----------
        actualizador.actualizarColecciones();

        // ---------- Assert ----------

        // Se agrega UNA sola relación Hecho–Colección
        verify(repositoryHechoXColeccion, times(1))
                .save(any(HechoXColeccion.class));

        // Se persisten las colecciones
        verify(repositoryColecciones, times(1))
                .saveAll(anyList());

        // No se procesa gestor de solicitudes (ya no es responsabilidad)
        verifyNoInteractions(gestorSolicitudes);
    }

    @Test
    void actualizarColecciones_agregaHechosCorrespondientesEnDosColecciones() {
        // ---------- Arrange ----------

        // Hechos
        Hecho hNuevo1 = new Hecho("Nuevo1", "Desc", null, null, LocalDate.now(), null);
        hNuevo1.setId_hecho(1L);

        Hecho hNuevo2 = new Hecho("Nuevo2", "Desc", null, null, LocalDate.now(), null);
        hNuevo2.setId_hecho(2L);

        Hecho hExistente1 = new Hecho("Existente1", "Desc", null, null, LocalDate.now(), null);
        hExistente1.setId_hecho(3L);

        // Loaders
        when(loader1.obtenerHechos()).thenReturn(List.of(hNuevo1));
        when(loader2.obtenerHechos()).thenReturn(List.of(hNuevo2));

        // Normalizador
        when(normalizador.normalizar(anyList()))
                .thenReturn(List.of(hNuevo1, hNuevo2));

        // Repositorio de hechos
        when(repositoryHechos.findAll())
                .thenReturn(List.of(hExistente1, hNuevo1, hNuevo2));

        when(repositoryHechos.findFirstByTitulo(anyString()))
                .thenReturn(null);

        // Colecciones
        Coleccion colA = mock(Coleccion.class);
        when(colA.getId_coleccion()).thenReturn(10L);

        Coleccion colB = mock(Coleccion.class);
        when(colB.getId_coleccion()).thenReturn(20L);

        when(repositoryColecciones.findAll())
                .thenReturn(List.of(colA, colB));

        // Hechos ya existentes en la colección A
        HechoXColeccion hxcPrevio = new HechoXColeccion(hExistente1, colA, false);
        when(repositoryHechoXColeccion.findByColeccion(10L))
                .thenReturn(List.of(hxcPrevio));

        // Colección B comienza vacía
        when(repositoryHechoXColeccion.findByColeccion(20L))
                .thenReturn(Collections.emptyList());

        // Condiciones
        InterfaceCondicion condA = mock(InterfaceCondicion.class);
        InterfaceCondicion condB = mock(InterfaceCondicion.class);

        when(repositoryColecciones.findByIdCondiciones(10L))
                .thenReturn(List.of(condA));

        when(repositoryColecciones.findByIdCondiciones(20L))
                .thenReturn(List.of(condB));

        // Fuentes (irrelevantes para este test)
        when(repositoryFuentes.findFuentesByColeccion(anyLong()))
                .thenReturn(Collections.emptyList());

        // Filtrador: stubs generales (clave para strict stubbing)
        when(filtrador.devolverHechosDeFuentes(anyList(), anyList()))
                .thenReturn(List.of(hNuevo1, hNuevo2));

        when(filtrador.devolverHechosAPartirDe(anyList(), anyList()))
                .thenReturn(List.of(hNuevo1, hNuevo2));

        // Ninguna relación Hecho–Colección existe previamente
        when(repositoryHechoXColeccion.findByConjunto(anyLong(), anyLong()))
                .thenReturn(null);

        // ---------- Act ----------
        actualizador.actualizarColecciones();

        // ---------- Assert ----------

        // Se agregan relaciones para los hechos nuevos en ambas colecciones
        // (2 hechos nuevos × 2 colecciones = 4 relaciones)
        verify(repositoryHechoXColeccion, times(4))
                .save(any(HechoXColeccion.class));

        // Se persisten las colecciones
        verify(repositoryColecciones, times(1))
                .saveAll(anyList());

        // El gestor de solicitudes NO es responsabilidad de este componente
        verifyNoInteractions(gestorSolicitudes);
    }
}