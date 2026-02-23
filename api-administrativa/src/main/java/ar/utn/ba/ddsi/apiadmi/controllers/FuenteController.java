package ar.utn.ba.ddsi.apiadmi.controllers;

import ar.utn.ba.ddsi.apiadmi.models.entities.fuente.Fuente;
import ar.utn.ba.ddsi.apiadmi.servicies.SolicitudesService;
import ar.utn.ba.ddsi.apiadmi.servicies.interfaces.IFuenteServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/fuentes")
@Tag(
        name = "Fuentes",
        description = "Endpoints para la gestión de fuentes"
)
public class FuenteController {

    @Autowired
    private IFuenteServices fuenteService;

    @Operation(
            summary = "Obtener todas las fuentes",
            description = "Devuelve un listado con todas las fuentes disponibles en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de fuentes obtenido correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = Fuente.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    @GetMapping
    private ResponseEntity<List<Fuente>> obtenerFuentes(){


        List<Fuente> fuentes = this.fuenteService.obtenerFuentes();
        if (fuentes.isEmpty()) {
            log.warn("No se encontraron etiquetas");
        } else {
            log.debug("Se encontraron {} etiquetas", fuentes.size());
        }
        return ResponseEntity.ok(fuentes);
    }
}
