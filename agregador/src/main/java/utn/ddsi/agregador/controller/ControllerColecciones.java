package utn.ddsi.agregador.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import utn.ddsi.agregador.domain.coleccion.Coleccion;
import utn.ddsi.agregador.service.ServiceColecciones;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/colecciones")
@Tag(
        name = "Colecciones",
        description = "Operaciones relacionadas con la gestión de colecciones"
)
public class ControllerColecciones {

    @Autowired
    private ServiceColecciones service;

    @Operation(
            summary = "Buscar colección por ID",
            description = "Obtiene una colección junto con sus datos asociados a partir de su identificador"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Colección encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Coleccion.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Colección no encontrada",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public List<Coleccion> buscarPorId(
            @Parameter(
                    description = "ID de la colección a buscar",
                    example = "1",
                    required = true
            )
            @PathVariable Long id
    ) {
        return service.buscarPorID(id);
    }

    @Operation(
            summary = "Crear una nueva colección",
            description = "Crea una nueva colección con su título, descripción, fuentes y configuración de consenso"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Colección creada correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request inválido (colección nula)"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno al crear la colección"
            )
    })
    @PostMapping
    public ResponseEntity.BodyBuilder agregarColeccion(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto colección a crear",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = Coleccion.class)
                    )
            )
            @RequestBody Coleccion coleccion
    ) {

        log.info("CONTROLLER : Iniciando petición de Agregar una nueva colección");

        if (coleccion == null) {
            log.error("Error Técnico: El objeto de la petición es NULL");
            return ResponseEntity.badRequest();
        }

        try {
            service.agregar(coleccion);
        } catch (Exception e) {
            log.error("ERROR TÉCNICO: Fallo al intentar agregar la colección", e);
            return ResponseEntity.internalServerError();
        }

        log.info("CONTROLLER : Se realizó con éxito AGREGAR COLECCIÓN");
        return ResponseEntity.ok();
    }
}