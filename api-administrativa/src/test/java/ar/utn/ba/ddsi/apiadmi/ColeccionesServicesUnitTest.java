package ar.utn.ba.ddsi.apiadmi;

import ar.utn.ba.ddsi.apiadmi.models.dtos.ColeccionDto;
import ar.utn.ba.ddsi.apiadmi.models.dtos.input.ColeccionInput;
import ar.utn.ba.ddsi.apiadmi.models.dtos.input.CondicionInput;
import ar.utn.ba.ddsi.apiadmi.models.entities.coleccion.Coleccion;
import ar.utn.ba.ddsi.apiadmi.models.entities.condiciones.CondicionTitulo;
import ar.utn.ba.ddsi.apiadmi.models.entities.condiciones.InterfaceCondicion;
import ar.utn.ba.ddsi.apiadmi.models.entities.fuente.Fuente;
import ar.utn.ba.ddsi.apiadmi.models.factory.ColeccionFactory;
import ar.utn.ba.ddsi.apiadmi.models.repository.IColeccionRepository;
import ar.utn.ba.ddsi.apiadmi.servicies.CategoriaService;
import ar.utn.ba.ddsi.apiadmi.servicies.ColeccionesServices;
import ar.utn.ba.ddsi.apiadmi.servicies.CondicionService;
import ar.utn.ba.ddsi.apiadmi.servicies.EtiquetaService;
import ar.utn.ba.ddsi.apiadmi.servicies.interfaces.IFuenteServices;
import ar.utn.ba.ddsi.apiadmi.utils.EnumTipoDeAlgoritmo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ColeccionesServicesUnitTest {

    @Mock
    private IColeccionRepository coleccionRepository;

    @Mock
    private IFuenteServices fuenteService;

    @Mock
    private ColeccionFactory coleccionFactory;

    @Mock
    private CategoriaService categoriaService;

    @Mock
    private EtiquetaService etiquetaService;

    @Mock
    private CondicionService condicionService;

    @InjectMocks
    private ColeccionesServices coleccionesServices;


    // ============================================================
    // TEST obtenerColecciones()
    // ============================================================
    @Test
    void obtenerColecciones_retornaListaDto() {

        // Mock de entidad
        Coleccion cole = new Coleccion();
        cole.setId_coleccion(1L);
        cole.setTitulo("Titulo Test");
        cole.setDescripcion("Desc");
        cole.setTipoDeAlgoritmo(EnumTipoDeAlgoritmo.ABSOLUTA);

        // Condiciones
        InterfaceCondicion condMock = Mockito.mock(InterfaceCondicion.class);
        Mockito.when(condMock.getId_condicion()).thenReturn(10L);
        Mockito.when(condMock.tipo()).thenReturn("titulo");
        Mockito.when(condMock.valor()).thenReturn("abc");
        cole.setCondicionDePertenencia(List.of(condMock));

        // Fuentes
        Fuente f = new Fuente();
        f.setNombre("Fuente A");
        cole.setFuentes(List.of(f));

        // Mock repositorio
        Mockito.when(coleccionRepository.findAll()).thenReturn(List.of(cole));

        // Mock llamada interna a fuenteService
        Mockito.when(fuenteService.buscarPorNombre("Fuente A"))
                .thenReturn(f);

        List<ColeccionDto> resultado = coleccionesServices.obtenerColecciones();

        assertEquals(1, resultado.size());
        assertEquals("Titulo Test", resultado.get(0).getTitulo());
        assertEquals(1, resultado.get(0).getFuentes().size());
    }


    // ============================================================
    // TEST agregar()
    // ============================================================
    @Test
    void agregar_guardaColeccion() {

        ColeccionInput input = new ColeccionInput();
        input.setTitulo("Nueva cole");
        input.setDescripcion("Desc");
        input.setAlgoritmoConcenso("ABSOLUTA");
        input.setFuentes(List.of("Fuente1"));

        CondicionInput condInput = new CondicionInput();
        condInput.setTipo("titulo");
        condInput.setValor("abc");
        input.setCriterios(List.of(condInput));

        // Mocks
        Fuente fuenteMock = new Fuente();
        fuenteMock.setNombre("Fuente1");

        Mockito.when(fuenteService.buscarPorNombre("Fuente1"))
                .thenReturn(fuenteMock);

        InterfaceCondicion condMock = new CondicionTitulo("abc");


        Coleccion coleMock = new Coleccion();
        Mockito.when(coleccionFactory.crearColeccion(input))
                .thenReturn(coleMock);

        coleccionesServices.agregar(input);

        Mockito.verify(coleccionRepository, Mockito.times(1))
                .save(coleMock);
    }


    // ============================================================
    // TEST encontrarPorId()
    // ============================================================
    @Test
    void encontrarPorId_retornaSiExiste() {
        Coleccion c = new Coleccion();
        c.setId_coleccion(1L);

        Mockito.when(coleccionRepository.findById(1L))
                .thenReturn(Optional.of(c));

        Coleccion result = coleccionesServices.encontrarPorId(1L);

        assertEquals(1L, result.getId_coleccion());
    }

    @Test
    void encontrarPorId_lanzaSiNoExiste() {

        Mockito.when(coleccionRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> coleccionesServices.encontrarPorId(1L));
    }


    // ============================================================
    // TEST eliminar()
    // ============================================================
    @Test
    void eliminar_coleccionNoExiste_lanzaException() {

        Mockito.when(coleccionRepository.existsById(5L))
                .thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            coleccionesServices.eliminar(5L);
        });

        Mockito.verify(coleccionRepository, Mockito.never())
                .deleteById(Mockito.anyLong());
    }




    // ============================================================
    // TEST cargarOCrearCondicion()
    // ============================================================
    @Test
    void cargarOCrearCondicion_creaNuevaSiNoTieneId() {

        CondicionInput input = new CondicionInput();
        input.setTipo("titulo");
        input.setValor("abc");

        InterfaceCondicion cond = coleccionesServices.cargarOCrearCondicion(input);

        assertTrue(cond instanceof CondicionTitulo);
        assertEquals("abc", cond.valor());
    }


    // ============================================================
    // TEST actualizar() (versión simplificada)
    // ============================================================
    @Test
    void actualizar_modificaColeccionExistente() {

        // Estado original
        Coleccion cole = new Coleccion();
        cole.setId_coleccion(1L);
        cole.setTitulo("Viejo");
        cole.setFuentes(new ArrayList<>());
        cole.setCondicionDePertenencia(new ArrayList<>());

        Mockito.when(coleccionRepository.findById(1L))
                .thenReturn(Optional.of(cole));

        // Input de actualización
        ColeccionInput input = new ColeccionInput();
        input.setTitulo("Nuevo");
        input.setDescripcion("Nueva desc");
        input.setAlgoritmoConcenso("DEFAULT");
        input.setFuentes(List.of("Fuente1"));

        CondicionInput condInput = new CondicionInput();
        condInput.setTipo("titulo");
        condInput.setValor("xyz");
        input.setCriterios(List.of(condInput));

        Mockito.when(fuenteService.buscarPorNombre("Fuente1"))
                .thenReturn(new Fuente());



        // Ejecutar
        coleccionesServices.actualizar(1L, input);

        assertEquals("Nuevo", cole.getTitulo());
        assertEquals("Nueva desc", cole.getDescripcion());
        assertEquals(1, cole.getFuentes().size());

        Mockito.verify(coleccionRepository).save(cole);
    }
}
