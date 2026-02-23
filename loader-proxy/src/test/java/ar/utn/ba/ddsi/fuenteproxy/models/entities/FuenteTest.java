package ar.utn.ba.ddsi.fuenteproxy.models.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FuenteTest {

    @Test
    void constructor_conTodosLosParametros_creaFuenteCorrectamente() {
        // Act
        Fuente fuente = new Fuente(1L, "Fuente Test", "http://test.com", EnumTipoFuente.METAMAPA);

        // Assert
        assertEquals(1L, fuente.getId_fuente());
        assertEquals("Fuente Test", fuente.getNombre());
        assertEquals("http://test.com", fuente.getUrl());
        assertEquals(EnumTipoFuente.METAMAPA, fuente.getTipoFuente());
    }

    @Test
    void constructor_sinId_creaFuenteCorrectamente() {
        // Act
        Fuente fuente = new Fuente("Fuente Test", "http://test.com", EnumTipoFuente.DEMO);

        // Assert
        assertEquals("Fuente Test", fuente.getNombre());
        assertEquals("http://test.com", fuente.getUrl());
        assertEquals(EnumTipoFuente.DEMO, fuente.getTipoFuente());
    }

    @Test
    void constructorVacio_creaFuenteConCamposNulos() {
        // Act
        Fuente fuente = new Fuente();

        // Assert
        assertNull(fuente.getNombre());
        assertNull(fuente.getUrl());
        assertNull(fuente.getTipoFuente());
    }

    @Test
    void setters_asignanValoresCorrectamente() {
        // Arrange
        Fuente fuente = new Fuente();

        // Act
        fuente.setId_fuente(5L);
        fuente.setNombre("Nombre Nuevo");
        fuente.setUrl("http://nueva.url.com");
        fuente.setTipoFuente(EnumTipoFuente.ESTATICA);

        // Assert
        assertEquals(5L, fuente.getId_fuente());
        assertEquals("Nombre Nuevo", fuente.getNombre());
        assertEquals("http://nueva.url.com", fuente.getUrl());
        assertEquals(EnumTipoFuente.ESTATICA, fuente.getTipoFuente());
    }

    @Test
    void tipoFuente_metamapa_esValido() {
        // Arrange
        Fuente fuente = new Fuente("Test", "http://test.com", EnumTipoFuente.METAMAPA);

        // Assert
        assertEquals("METAMAPA", fuente.getTipoFuente().name());
    }

    @Test
    void tipoFuente_demo_esValido() {
        // Arrange
        Fuente fuente = new Fuente("Test", "http://test.com", EnumTipoFuente.DEMO);

        // Assert
        assertEquals("DEMO", fuente.getTipoFuente().name());
    }

    @Test
    void tipoFuente_estatica_esValido() {
        // Arrange
        Fuente fuente = new Fuente("Test", "http://test.com", EnumTipoFuente.ESTATICA);

        // Assert
        assertEquals("ESTATICA", fuente.getTipoFuente().name());
    }
}
