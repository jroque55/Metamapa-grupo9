package ar.utn.ba.ddsi.apiadmi;

import ar.utn.ba.ddsi.apiadmi.controllers.CollecionController;
import ar.utn.ba.ddsi.apiadmi.models.dtos.ColeccionDto;
import ar.utn.ba.ddsi.apiadmi.servicies.ColeccionesServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CollecionController.class)
class CollecionControllerIntegrationTest {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private ColeccionesServices coleccionesServices;

    @Test
    void obtenerColecciones_returnsOk() throws Exception {
        when(coleccionesServices.obtenerColecciones()).thenReturn(List.of(new ColeccionDto()));

        mvc.perform(get("/colecciones").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

