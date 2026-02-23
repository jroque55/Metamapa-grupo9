package ar.utn.ba.ddsi.apiadmi.controllers;

import ar.utn.ba.ddsi.apiadmi.servicies.HechoServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/hechos")
@Tag(
        name = "Hechos",
        description = "Endpoints para la gestión de hechos"
)
public class HechosController {
    @Autowired
    private HechoServices hechoService;

    @PutMapping("/{id}/etiqueta")
    @Operation(
            summary = "Asignar etiqueta a un hecho",
            description = "Asigna o actualiza la etiqueta asociada a un hecho existente"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Etiqueta asignada correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Hecho no encontrado"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Etiqueta inválida"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<Void> asignarEtiqueta(
            @Parameter(
                    description = "ID del hecho al que se le asigna la etiqueta",
                    example = "10",
                    required = true
            )
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nombre de la etiqueta a asignar",
                    required = true,
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(
                                    type = "string",
                                    example = "Historia"
                            )
                    )
            )
            @RequestBody String etiqueta) {

        log.info("Empezando proceso de asignación de etiqueta '{}' al hecho con ID {}", etiqueta, id);
        hechoService.actualizarEtiqueta(id, etiqueta);

        return ResponseEntity.noContent().build();
    }
}
