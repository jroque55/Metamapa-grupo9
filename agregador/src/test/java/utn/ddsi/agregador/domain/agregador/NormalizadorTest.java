package utn.ddsi.agregador.domain.agregador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import utn.ddsi.agregador.domain.hecho.Adjunto;
import utn.ddsi.agregador.domain.hecho.Categoria;
import utn.ddsi.agregador.domain.hecho.Hecho;
import utn.ddsi.agregador.domain.hecho.Provincia;
import utn.ddsi.agregador.domain.hecho.Ubicacion;
import utn.ddsi.agregador.domain.fuentes.Fuente;
import utn.ddsi.agregador.repository.IRepositoryCategorias;
import utn.ddsi.agregador.repository.IRepositoryProvincias;
import utn.ddsi.agregador.repository.IRepositoryUbicacion;
import utn.ddsi.agregador.utils.EnumTipoFuente;
import utn.ddsi.agregador.utils.TipoMedia;

class NormalizadorTest {
    IRepositoryCategorias repoCategorias;
    IRepositoryProvincias repoProvincias;
    IRepositoryUbicacion repoUbicacion;

    private final Clock clock = Clock.fixed(Instant.parse("2025-01-10T00:00:00Z"), ZoneOffset.UTC);
    Normalizador normalizador;
    @BeforeEach
    void beforeEach() {

        repoCategorias = mock(IRepositoryCategorias.class);
        repoProvincias = mock(IRepositoryProvincias.class);
        repoUbicacion = mock(IRepositoryUbicacion.class);

        when(repoProvincias.findFirstByNombre(anyString())).thenReturn(null);
        when(repoProvincias.save(any())).thenAnswer(inv -> inv.getArgument(0));

        normalizador = new Normalizador(clock, repoCategorias, repoProvincias, repoUbicacion);
    }
    @Test
    void normalizaCategoriaFechasYProvincia() {
        Categoria incendioForestal = new Categoria("Incendio forestal");
        incendioForestal.setId_categoria(1L); // simulamos ID generado por DB
        when(repoCategorias.findByNombre("Fuego forestal")).thenReturn(incendioForestal);
        when(repoCategorias.save(any())).thenAnswer(invocation -> {
            Categoria cat = invocation.getArgument(0);
            cat.setId_categoria(2L); // simulamos ID si se guarda nueva
            return cat;
        });

        Provincia cordoba = new Provincia();
        cordoba.setNombre("Cordoba");
        cordoba.setPais("Argentina");
        when(repoProvincias.findFirstByNombre("Cordoba")).thenReturn(cordoba);
        when(repoProvincias.save(any())).thenReturn(cordoba);

        Hecho hecho = new Hecho();
        hecho.setTitulo("  Incendio forestal en cordoba   ");
        hecho.setDescripcion("  foco activo en zona norte   ");
        hecho.setCategoria(new Categoria("Fuego forestal")); // sin ID
        hecho.setUbicacion(new Ubicacion(-31.4f, -64.2f));
        hecho.setFecha(LocalDate.of(2025, 1, 12));
        hecho.setFechaDeCarga(LocalDateTime.of(2025, 1,9,5,10));

        List<Hecho> normalizados = normalizador.normalizar(List.of(hecho));

        assertEquals(1, normalizados.size());
        Hecho resultado = normalizados.get(0);

        assertNotNull(resultado.getCategoria());
        assertEquals("Incendio forestal", resultado.getCategoria().getNombre());
        assertNotNull(resultado.getCategoria().getId_categoria());

        assertEquals(LocalDate.of(2025, 1, 10), resultado.getFecha());
        assertEquals(LocalDateTime.of(2025, 1, 10,0,0), resultado.getFechaDeCarga());

        assertEquals("Incendio forestal en cordoba", resultado.getTitulo());

        assertNotNull(resultado.getUbicacion());
        assertNotNull(resultado.getUbicacion().getProvincia());
        assertEquals("Cordoba", resultado.getUbicacion().getProvincia().getNombre());
    }

    @Test
    void fusionaDuplicadosPrivilegiandoFuenteMasConfiable() {
        Categoria incendioForestal = new Categoria("Incendio forestal");
        incendioForestal.setId_categoria(1L); // simula ID
        when(repoCategorias.findByNombre(anyString())).thenAnswer(inv -> {
            String nombre = inv.getArgument(0);
            if (nombre.equalsIgnoreCase("Incendio forestal")) return incendioForestal;
            return null;
        });
        when(repoCategorias.save(any())).thenAnswer(inv -> {
            Categoria cat = inv.getArgument(0);
            if (cat.getId_categoria() == null) cat.setId_categoria(2L);
            return cat;
        });

        Hecho base = new Hecho();
        base.setTitulo("Incendio en las sierras");
        base.setDescripcion("Foco contenido");
        base.setCategoria(new Categoria("Incendio forestal")); // sin ID, se normaliza
        base.setUbicacion(new Ubicacion(-31.40f, -64.20f));
        base.setFecha(LocalDate.of(2025, 1, 8));
        base.setFechaDeCarga(LocalDateTime.of(2025, 1, 8,5,10));
        base.setAdjuntos(List.of(new Adjunto(0, TipoMedia.IMAGEN, "https://ejemplo/img.jpg")));
        Fuente fuenteDinamica = new Fuente();
        fuenteDinamica.setNombre("Fuente comunitaria");
        fuenteDinamica.setUrl("https://comunidad");
        fuenteDinamica.setTipoFuente(EnumTipoFuente.DINAMICA);
        base.setFuente(fuenteDinamica);

        Hecho candidato = new Hecho();
        candidato.setTitulo("  incendio en las sierras  ");
        candidato.setDescripcion("Fuego en zona de dificil acceso con 3 brigadas trabajando.");
        candidato.setCategoria(new Categoria("Fuego forestal")); // sin ID
        candidato.setUbicacion(new Ubicacion(-31.401f, -64.201f));
        candidato.setFecha(LocalDate.of(2025, 1, 9));
        candidato.setFechaDeCarga(LocalDateTime.of(2025, 1, 9,9,10));
        candidato.setAdjuntos(List.of(new Adjunto(0, TipoMedia.VIDEO, "https://ejemplo/video.mp4")));
        Fuente fuenteEstatica = new Fuente();
        fuenteEstatica.setNombre("Base oficial");
        fuenteEstatica.setUrl("https://datos");
        fuenteEstatica.setTipoFuente(EnumTipoFuente.ESTATICA);
        candidato.setFuente(fuenteEstatica);

        List<Hecho> normalizados = normalizador.normalizar(List.of(base, candidato));

        assertEquals(1, normalizados.size());
        Hecho resultado = normalizados.get(0);
        assertEquals("Incendio forestal", resultado.getCategoria().getNombre());
        assertNotNull(resultado.getCategoria().getId_categoria());
        assertTrue(resultado.getDescripcion().contains("dificil acceso"));
        assertEquals(EnumTipoFuente.ESTATICA, resultado.getFuente().getTipoFuente());
        assertNotNull(resultado.getAdjuntos());
        assertEquals(2, resultado.getAdjuntos().size());
    }

    @Test
    void descartaUbicacionesInvalidas() {
        // --- Mock Categoria con ID ---
        Categoria contaminacion = new Categoria("Contaminacion");
        contaminacion.setId_categoria(1L); // simulamos ID asignado
        when(repoCategorias.findByNombre(anyString())).thenReturn(contaminacion);
        when(repoCategorias.save(any())).thenAnswer(inv -> {
            Categoria cat = inv.getArgument(0);
            if (cat.getId_categoria() == null) cat.setId_categoria(2L);
            return cat;
        });

        // --- Hecho con ubicación inválida ---
        Hecho hecho = new Hecho();
        hecho.setTitulo("hecho sin ubicacion valida");
        hecho.setCategoria(new Categoria("contaminacion")); // sin ID
        hecho.setUbicacion(new Ubicacion(120f, -200f)); // inválida
        hecho.setFecha(LocalDate.of(2025, 1, 5));
        hecho.setFechaDeCarga(LocalDateTime.of(2025, 1, 6,9,10));

        List<Hecho> normalizados = normalizador.normalizar(List.of(hecho));

        assertEquals(1, normalizados.size());
        assertNull(normalizados.get(0).getUbicacion());
    }

    @Test
    void asignaProvinciaDesdePoligonoDelCsv() {
        // --- Mock Categoria ---
        Categoria incendio = new Categoria("incendio");
        incendio.setId_categoria(1L); // simulamos ID generado por DB
        when(repoCategorias.findByNombre("incendio")).thenReturn(incendio);
        when(repoCategorias.save(any())).thenAnswer(invocation -> {
            Categoria cat = invocation.getArgument(0);
            cat.setId_categoria(2L); // simulamos ID si se guarda nueva
            return cat;
        });

        // --- Mock Provincia ---
        when(repoProvincias.findFirstByNombre("Ciudad Autonoma de Buenos Aires")).thenReturn(null);
        Provincia caba = new Provincia();
        caba.setNombre("Ciudad Autonoma de Buenos Aires");
        caba.setPais("Argentina");
        when(repoProvincias.save(any())).thenReturn(caba);

        // --- Creamos el hecho ---
        Hecho hecho = new Hecho();
        hecho.setTitulo("situacion en el microcentro");
        hecho.setCategoria(new Categoria("incendio")); // sin ID
        hecho.setUbicacion(new Ubicacion(-34.6037f, -58.3816f));
        hecho.setFecha(LocalDate.of(2025, 1, 7));
        hecho.setFechaDeCarga(LocalDateTime.of(2025, 1, 7, 9, 10));

        // --- Ejecutamos normalización ---
        List<Hecho> normalizados = normalizador.normalizar(List.of(hecho));

        // --- Assertions ---
        assertEquals(1, normalizados.size());

        Hecho normalizado = normalizados.get(0);

        // Categoria
        assertNotNull(normalizado.getCategoria());
        assertEquals("Incendio", normalizado.getCategoria().getNombre());
        assertNotNull(normalizado.getCategoria().getId_categoria());

        // Ubicacion / Provincia
        assertNotNull(normalizado.getUbicacion());
        assertNotNull(normalizado.getUbicacion().getProvincia());
        assertEquals("Ciudad Autonoma de Buenos Aires", normalizado.getUbicacion().getProvincia().getNombre());
        assertEquals("Argentina", normalizado.getUbicacion().getProvincia().getPais());
    }
    @Test
    void reutilizaInstanciaDeProvinciaCacheada() {
        // Mock Categoria con ID
        Categoria incendio = new Categoria("incendio");
        incendio.setId_categoria(1L);
        when(repoCategorias.findByNombre(anyString())).thenReturn(incendio);
        when(repoCategorias.save(any())).thenAnswer(inv -> {
            Categoria cat = inv.getArgument(0);
            if (cat.getId_categoria() == null) cat.setId_categoria(2L);
            return cat;
        });

        // Hechos a normalizar
        Hecho primero = new Hecho();
        primero.setTitulo("evento en la capital");
        primero.setCategoria(new Categoria("incendio")); // sin ID
        primero.setUbicacion(new Ubicacion(-34.6037f, -58.3816f));
        primero.setFecha(LocalDate.of(2025, 1, 7));
        primero.setFechaDeCarga(LocalDateTime.of(2025, 1, 7,9,10));

        Hecho segundo = new Hecho();
        segundo.setTitulo("evento en puerto madero");
        segundo.setCategoria(new Categoria("incendio")); // sin ID
        segundo.setUbicacion(new Ubicacion(-34.6137f, -58.3616f));
        segundo.setFecha(LocalDate.of(2025, 1, 8));
        segundo.setFechaDeCarga(LocalDateTime.of(2025, 1, 8,9,10));

        List<Hecho> normalizados = normalizador.normalizar(List.of(primero, segundo));

        assertEquals(2, normalizados.size());
        Provincia provinciaUno = normalizados.get(0).getUbicacion().getProvincia();
        Provincia provinciaDos = normalizados.get(1).getUbicacion().getProvincia();
        assertNotNull(provinciaUno);
        assertNotNull(provinciaDos);
        assertSame(provinciaUno, provinciaDos);
    }

    @Test
    void ubicaHechosFueraDeArgentinaComoExterior() {
        // Mock de categoría con ID
        Categoria categoriaMock = new Categoria("Contaminacion");
        categoriaMock.setId_categoria(1L);
        when(repoCategorias.findByNombre(anyString())).thenReturn(categoriaMock);
        when(repoCategorias.save(any())).thenAnswer(inv -> {
            Categoria cat = inv.getArgument(0);
            if (cat.getId_categoria() == null) cat.setId_categoria(2L);
            return cat;
        });

        Hecho hecho = new Hecho();
        hecho.setTitulo("incidente internacional");
        hecho.setCategoria(new Categoria("contaminacion")); // sin ID
        hecho.setUbicacion(new Ubicacion(40.7128f, -74.0060f));
        hecho.setFecha(LocalDate.of(2025, 1, 4));
        hecho.setFechaDeCarga(LocalDateTime.of(2025, 1, 5,9,10));

        List<Hecho> normalizados = normalizador.normalizar(List.of(hecho));

        assertEquals(1, normalizados.size());
        Ubicacion ubicacion = normalizados.get(0).getUbicacion();
        assertNotNull(ubicacion);
        Provincia provincia = ubicacion.getProvincia();
        assertNotNull(provincia);
        assertEquals("EXTERIOR", provincia.getNombre());
        assertEquals("EXTERIOR", provincia.getPais());
    }
}
