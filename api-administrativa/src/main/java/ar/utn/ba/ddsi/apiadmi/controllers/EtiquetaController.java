package ar.utn.ba.ddsi.apiadmi.controllers;

import ar.utn.ba.ddsi.apiadmi.models.entities.hecho.Etiqueta;
import ar.utn.ba.ddsi.apiadmi.servicies.EtiquetaService;
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
@RequestMapping("/etiquetas")
@Tag(
        name = "Etiquetas",
        description = "Endpoints para la gestión de etiquetas"
)
public class EtiquetaController {

    @Autowired
    private EtiquetaService etiquetaService;

    @GetMapping
    @Operation(
            summary = "Obtener todas las etiquetas",
            description = "Devuelve un listado con todas las etiquetas disponibles en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de etiquetas obtenido correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = Etiqueta.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<List<Etiqueta>> getEtiquetas() {

        List<Etiqueta> etiquetas= etiquetaService.obtenerTodas();
        if (etiquetas.isEmpty()) {
            log.warn("No se encontraron etiquetas");
        } else {
            log.debug("Se encontraron {} etiquetas", etiquetas.size());
        }
        return ResponseEntity.ok(etiquetas);
    }
}