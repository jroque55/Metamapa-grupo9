package ar.utn.ba.ddsi.fuenteproxy.models.dtos;

import ar.utn.ba.ddsi.fuenteproxy.models.entities.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HechoOutputDTOTest {

    @Test
    void hechoOutputDTO_convierteHechoCorrectamente() {
        // Arrange
        Hecho hecho = crearHechoCompleto();

        HechoOutputDTO dto = new HechoOutputDTO();

        // Act
        dto.HechoOutputDTO(hecho);

        // Assert
        assertEquals("Titulo Test", dto.getTitulo());
        assertEquals("Descripcion Test", dto.getDescripcion());
        assertEquals("Categoria Test", dto.getCategoria());
        assertEquals("2025-06-15", dto.getFecha());
        assertEquals("-34.6", dto.getUbicacionLat());
        assertEquals("-58.4", dto.getUbicacionLon());
        assertEquals("TEXTO", dto.getTipoHecho());
    }

    @Test
    void hechoOutputDTO_convierteEtiquetaNulaCorrectamente() {
        // Arrange
        Hecho hecho = crearHechoSinEtiqueta();

        HechoOutputDTO dto = new HechoOutputDTO();

        // Act
        dto.HechoOutputDTO(hecho);

        // Assert
        assertNull(dto.getEtiqueta());
    }

    @Test
    void hechoOutputDTO_convierteFuenteCorrectamente() {
        // Arrange
        Hecho hecho = crearHechoCompleto();

        HechoOutputDTO dto = new HechoOutputDTO();

        // Act
        dto.HechoOutputDTO(hecho);

        // Assert
        assertNotNull(dto.getFuente());
        assertEquals("Fuente Test", dto.getFuente().getNombre());
        assertEquals("http://test.com", dto.getFuente().getUrl());
        assertEquals("METAMAPA", dto.getFuente().getTipoFuente());
    }

    @Test
    void constructorVacio_creaObjetoConCamposNulos() {
        // Act
        HechoOutputDTO dto = new HechoOutputDTO();

        // Assert
        assertNull(dto.getTitulo());
        assertNull(dto.getDescripcion());
        assertNull(dto.getCategoria());
    }

    @Test
    void setters_asignanValoresCorrectamente() {
        // Arrange
        HechoOutputDTO dto = new HechoOutputDTO();

        // Act
        dto.setTitulo("Titulo Set");
        dto.setDescripcion("Descripcion Set");
        dto.setCategoria("Categoria Set");
        dto.setFecha("2025-01-01");
        dto.setUbicacionLat("-30.0");
        dto.setUbicacionLon("-60.0");

        // Assert
        assertEquals("Titulo Set", dto.getTitulo());
        assertEquals("Descripcion Set", dto.getDescripcion());
        assertEquals("Categoria Set", dto.getCategoria());
        assertEquals("2025-01-01", dto.getFecha());
        assertEquals("-30.0", dto.getUbicacionLat());
        assertEquals("-60.0", dto.getUbicacionLon());
    }

    private Hecho crearHechoCompleto() {
        Categoria categoria = new Categoria("Categoria Test");
        Ubicacion ubicacion = new Ubicacion(-34.6f, -58.4f);
        Hecho hecho = new Hecho("Titulo Test", "Descripcion Test", categoria, ubicacion, LocalDate.of(2025, 6, 15));
        hecho.setFechaDeCarga(LocalDate.of(2025, 6, 16));
        hecho.setTipoHecho(EnumTipoHecho.TEXTO);
        
        Fuente fuente = new Fuente(1L, "Fuente Test", "http://test.com", EnumTipoFuente.METAMAPA);
        hecho.setFuente(fuente);
        
        return hecho;
    }

    private Hecho crearHechoSinEtiqueta() {
        Hecho hecho = crearHechoCompleto();
        // La etiqueta ya es null por defecto
        return hecho;
    }
}
