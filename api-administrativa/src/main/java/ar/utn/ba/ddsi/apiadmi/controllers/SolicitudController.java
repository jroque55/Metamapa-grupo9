package ar.utn.ba.ddsi.apiadmi.controllers;

import ar.utn.ba.ddsi.apiadmi.models.entities.admin.SolicitudEliminacion;
import ar.utn.ba.ddsi.apiadmi.servicies.SolicitudesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ar.utn.ba.ddsi.apiadmi.models.dtos.input.SolicitudInput;

@Slf4j
@RestController
@RequestMapping ("/solicitudes")
@Tag(
        name = "Solicitudes",
        description = "Endpoints para la gestión de solicitudes"
)
public class SolicitudController {

    @Autowired
    private SolicitudesService solicitudesService;

    @Operation(
            summary = "Actualizar el estado de una solicitud",
            description = "Actualiza el estado de una solicitud existente mediante su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Solicitud actualizada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SolicitudEliminacion.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Solicitud no encontrada"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos en la solicitud"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    @PutMapping("/{id}")
    public SolicitudEliminacion actualizarEstado(
            @Parameter(
                    description = "ID de la solicitud a actualizar",
                    example = "5",
                    required = true
            )
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos para actualizar el estado de la solicitud",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SolicitudInput.class)
                    )
            )
            @Valid @RequestBody SolicitudInput soli){
        log.info("Actualizando estado de la solicitud con ID: {}", id);
        SolicitudEliminacion solicitud =solicitudesService.actualizarEstado(id, soli);
        return ResponseEntity.ok(solicitud).getBody();
    }
    @Operation(
            summary = "Eliminar una solicitud",
            description = "Elimina una solicitud del sistema mediante su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Solicitud eliminada correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Solicitud no encontrada"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSolicitud(
            @Parameter(
                    description = "ID de la solicitud a eliminar",
                    example = "5",
                    required = true
            )
            @PathVariable Long id) {
        log.info("Eliminando solicitud con ID: {}", id);
        solicitudesService.eliminarSolicitud(id);
        return ResponseEntity.noContent().build();
    }

}
