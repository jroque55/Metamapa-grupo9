package ar.utn.ba.ddsi.fuenteproxy.Controller;

import ar.utn.ba.ddsi.fuenteproxy.Service.IHechoServices;
import ar.utn.ba.ddsi.fuenteproxy.models.dtos.FuenteDTO;
import ar.utn.ba.ddsi.fuenteproxy.models.dtos.HechoOutputDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = HechoController.class, useDefaultFilters = false)
@ContextConfiguration(classes = {HechoController.class})
@AutoConfigureMockMvc
class HechoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IHechoServices hechoServices;

    @Test
    void obtenerHechos_conHechosDisponibles_retornaListaDeHechos() throws Exception {
        // Arrange
        HechoOutputDTO hecho1 = crearHechoOutputDTO("Titulo 1", "Descripcion 1");
        HechoOutputDTO hecho2 = crearHechoOutputDTO("Titulo 2", "Descripcion 2");
        List<HechoOutputDTO> hechos = Arrays.asList(hecho1, hecho2);

        when(hechoServices.BuscarHechos()).thenReturn(hechos);

        // Act & Assert
        mockMvc.perform(get("/hechos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].titulo").value("Titulo 1"))
                .andExpect(jsonPath("$[1].titulo").value("Titulo 2"));

        verify(hechoServices, times(1)).BuscarHechos();
    }

    @Test
    void obtenerHechos_sinHechosDisponibles_retornaListaVacia() throws Exception {
        // Arrange
        when(hechoServices.BuscarHechos()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/hechos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(hechoServices, times(1)).BuscarHechos();
    }

    @Test
    void obtenerHechos_verificaCamposDelDTO() throws Exception {
        // Arrange
        HechoOutputDTO hecho = crearHechoOutputDTOCompleto();
        when(hechoServices.BuscarHechos()).thenReturn(List.of(hecho));

        // Act & Assert
        mockMvc.perform(get("/hechos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Titulo Test"))
                .andExpect(jsonPath("$[0].descripcion").value("Descripcion Test"))
                .andExpect(jsonPath("$[0].categoria").value("Categoria Test"))
                .andExpect(jsonPath("$[0].fecha").value("2025-06-15"))
                .andExpect(jsonPath("$[0].ubicacionLat").value("-34.6"))
                .andExpect(jsonPath("$[0].ubicacionLon").value("-58.4"))
                .andExpect(jsonPath("$[0].tipoHecho").value("TEXTO"))
                .andExpect(jsonPath("$[0].fuente.nombre").value("Fuente Test"))
                .andExpect(jsonPath("$[0].fuente.url").value("http://test.com"));
    }

    private HechoOutputDTO crearHechoOutputDTO(String titulo, String descripcion) {
        HechoOutputDTO dto = new HechoOutputDTO();
        dto.setTitulo(titulo);
        dto.setDescripcion(descripcion);
        dto.setCategoria("Categoria Test");
        dto.setFecha("2025-06-15");
        dto.setUbicacionLat("-34.6");
        dto.setUbicacionLon("-58.4");
        dto.setTipoHecho("TEXTO");
        
        FuenteDTO fuenteDTO = new FuenteDTO();
        fuenteDTO.setNombre("Fuente Test");
        fuenteDTO.setUrl("http://test.com");
        fuenteDTO.setTipoFuente("METAMAPA");
        dto.setFuente(fuenteDTO);
        
        return dto;
    }

    private HechoOutputDTO crearHechoOutputDTOCompleto() {
        HechoOutputDTO dto = new HechoOutputDTO();
        dto.setTitulo("Titulo Test");
        dto.setDescripcion("Descripcion Test");
        dto.setCategoria("Categoria Test");
        dto.setFecha("2025-06-15");
        dto.setFechaDeCarga("2025-06-16");
        dto.setUbicacionLat("-34.6");
        dto.setUbicacionLon("-58.4");
        dto.setEtiqueta("Etiqueta Test");
        dto.setTipoHecho("TEXTO");
        
        FuenteDTO fuenteDTO = new FuenteDTO();
        fuenteDTO.setNombre("Fuente Test");
        fuenteDTO.setUrl("http://test.com");
        fuenteDTO.setTipoFuente("METAMAPA");
        dto.setFuente(fuenteDTO);
        
        dto.setAdjuntos(Collections.emptyList());
        
        return dto;
    }
}
