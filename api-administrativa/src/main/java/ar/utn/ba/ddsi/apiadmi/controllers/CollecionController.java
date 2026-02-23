package ar.utn.ba.ddsi.apiadmi.controllers;

import ar.utn.ba.ddsi.apiadmi.models.dtos.ColeccionDto;
import ar.utn.ba.ddsi.apiadmi.models.dtos.input.ColeccionInput;
import ar.utn.ba.ddsi.apiadmi.models.entities.coleccion.Coleccion;
import ar.utn.ba.ddsi.apiadmi.servicies.ColeccionesServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping ("/colecciones")
@Tag(
        name = "Colecciones",
        description = "Endpoints para la gestión de colecciones"
)
public class CollecionController {

    @Autowired
    private ColeccionesServices coleccionService;

    /*private  CollecionController(ColeccionesServices coleccionService){

        this.coleccionService= coleccionService;
    }*/

    @Operation(
            summary = "Crear una nueva colección",
            description = "Agrega una nueva colección al sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Colección creada correctamente"
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
   @PostMapping
    //EL @Valid se encarga de validar el objeto ColeccionInput segun las anotaciones definidad en esa clase.
    public ResponseEntity<Object> agregarColeccion(@NonNull @Valid @RequestBody
                                     @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                             description = "Datos de la colección a crear",
                                             required = true,
                                             content = @Content(
                                                     mediaType = "application/json",
                                                     schema = @Schema(implementation = ColeccionInput.class)
                                             )
                                     )
                                     ColeccionInput coleccion){


        coleccionService.agregar(coleccion);

        log.info("Se agregó una nueva colección: {}", coleccion.getTitulo());

        return ResponseEntity.status(201).build();



    }

    @Operation(
            summary = "Obtener todas las colecciones",
            description = "Devuelve una lista con todas las colecciones registradas"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de colecciones obtenido correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ColeccionDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    @GetMapping
    public ResponseEntity<Object> obtenerColecciones(){

        log.info("Obteniendo todas las colecciones");
        List<ColeccionDto> colecciones = this.coleccionService.obtenerColecciones();

        if(colecciones.isEmpty()){
            log.info("No se encontraron colecciones");
        }
        return ResponseEntity.ok(colecciones);

    }

    @Operation(
            summary = "Actualizar una colección",
            description = "Actualiza los datos de una colección existente mediante su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Colección actualizada correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Colección no encontrada"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            )
    })
    @PutMapping ("/{id}")
    public ResponseEntity<Object> actualizarColeccion(
            @Parameter(
                    description = "ID de la colección a actualizar",
                    example = "1",
                    required = true
            )@PathVariable Long id ,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados de la colección",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ColeccionInput.class)
                    )
            )
            @RequestBody ColeccionInput coleccionInput) {

        log.info("Actualizando colección con ID {}: {}", id, coleccionInput.getTitulo());

        this.coleccionService.actualizar(id,coleccionInput);
        return ResponseEntity.noContent().build();



    }
    @Operation(
            summary = "Eliminar una colección",
            description = "Elimina una colección del sistema mediante su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Colección eliminada correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Colección no encontrada"
            )
    })
    @DeleteMapping ("/{id}")
    public ResponseEntity<Object> eliminarColeccion(
            @Parameter(
                    description = "ID de la colección a eliminar",
                    example = "1",
                    required = true
            )
            @PathVariable Long id ){

        log.info("Eliminando colección con ID {}", id);
        this.coleccionService.eliminar(id);
        return ResponseEntity.ok().build();
    }


}
