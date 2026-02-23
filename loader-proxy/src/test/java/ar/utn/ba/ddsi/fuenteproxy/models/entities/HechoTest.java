package ar.utn.ba.ddsi.fuenteproxy.models.entities;

import ar.utn.ba.ddsi.fuenteproxy.models.dtos.HechoInputDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class HechoTest {

    @Test
    void constructor_conParametrosBasicos_creaHechoCorrectamente() {
        // Arrange
        String titulo = "Titulo Test";
        String descripcion = "Descripcion Test";
        Categoria categoria = new Categoria("Categoria Test");
        Ubicacion ubicacion = new Ubicacion(-34.6f, -58.4f);
        LocalDate fecha = LocalDate.of(2025, 6, 15);

        // Act
        Hecho hecho = new Hecho(titulo, descripcion, categoria, ubicacion, fecha);

        // Assert
        assertEquals(titulo, hecho.getTitulo());
        assertEquals(descripcion, hecho.getDescripcion());
        assertEquals(categoria, hecho.getCategoria());
        assertEquals(ubicacion, hecho.getUbicacion());
        assertEquals(fecha, hecho.getFecha());
    }

    @Test
    void constructor_conHechoInputDTO_creaHechoCorrectamente() {
        // Arrange
        HechoInputDTO dto = new HechoInputDTO();
        dto.setTitulo("Titulo DTO");
        dto.setDescripcion("Descripcion DTO");
        dto.setCategoria("Categoria DTO");
        dto.setUbicacionLat("-34.6");
        dto.setUbicacionLon("-58.4");
        dto.setFecha("2025-06-15");
        dto.setFechaDeCarga("2025-06-16");
        dto.setEtiqueta("Etiqueta DTO");
        dto.setTipoHecho("TEXTO");

        // Act
        Hecho hecho = new Hecho(dto);

        // Assert
        assertEquals("Titulo DTO", hecho.getTitulo());
        assertEquals("Descripcion DTO", hecho.getDescripcion());
        assertEquals("Categoria DTO", hecho.getCategoria().getNombre());
        assertEquals(-34.6f, hecho.getUbicacion().getLatitud(), 0.01);
        assertEquals(-58.4f, hecho.getUbicacion().getLongitud(), 0.01);
        assertEquals(LocalDate.of(2025, 6, 15), hecho.getFecha());
        assertEquals(LocalDate.of(2025, 6, 16), hecho.getFechaDeCarga());
        assertEquals(EnumTipoHecho.TEXTO, hecho.getTipoHecho());
    }

    @Test
    void setFuente_asignaFuenteCorrectamente() {
        // Arrange
        Hecho hecho = new Hecho("Titulo", "Desc", new Categoria("Cat"), new Ubicacion(0, 0), LocalDate.now());
        Fuente fuente = new Fuente(1L, "Fuente Test", "http://test.com", EnumTipoFuente.METAMAPA);

        // Act
        hecho.setFuente(fuente);

        // Assert
        assertEquals(fuente, hecho.getFuente());
    }

    @Test
    void setTipoHecho_asignaTipoCorrectamente() {
        // Arrange
        Hecho hecho = new Hecho("Titulo", "Desc", new Categoria("Cat"), new Ubicacion(0, 0), LocalDate.now());

        // Act
        hecho.setTipoHecho(EnumTipoHecho.MULTIMEDIA);

        // Assert
        assertEquals(EnumTipoHecho.MULTIMEDIA, hecho.getTipoHecho());
    }

    @Test
    void cambiarTitulo_modificaTituloCorrectamente() {
        // Arrange
        Hecho hecho = new Hecho("Titulo Original", "Desc", new Categoria("Cat"), new Ubicacion(0, 0), LocalDate.now());

        // Act
        hecho.cambiarTitulo("Titulo Nuevo");

        // Assert
        assertEquals("Titulo Nuevo", hecho.getTitulo());
    }

    @Test
    void cambiarDescripcion_modificaDescripcionCorrectamente() {
        // Arrange
        Hecho hecho = new Hecho("Titulo", "Descripcion Original", new Categoria("Cat"), new Ubicacion(0, 0), LocalDate.now());

        // Act
        hecho.cambiarDescripcion("Descripcion Nueva");

        // Assert
        assertEquals("Descripcion Nueva", hecho.getDescripcion());
    }

    @Test
    void cambiarUbicacion_modificaUbicacionCorrectamente() {
        // Arrange
        Hecho hecho = new Hecho("Titulo", "Desc", new Categoria("Cat"), new Ubicacion(0, 0), LocalDate.now());

        // Act
        hecho.cambiarUbicacion("-34.6", "-58.4");

        // Assert
        assertEquals(-34.6f, hecho.getUbicacion().getLatitud(), 0.01);
        assertEquals(-58.4f, hecho.getUbicacion().getLongitud(), 0.01);
    }

    @Test
    void getAdjuntos_retornaListaVaciaPorDefecto() {
        // Arrange
        Hecho hecho = new Hecho("Titulo", "Desc", new Categoria("Cat"), new Ubicacion(0, 0), LocalDate.now());

        // Assert
        assertNotNull(hecho.getAdjuntos());
        assertTrue(hecho.getAdjuntos().isEmpty());
    }
}
