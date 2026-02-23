package ar.utn.ba.ddsi.apiadmi.controllers;

import ar.utn.ba.ddsi.apiadmi.models.dtos.input.CategoriaDTO;
import ar.utn.ba.ddsi.apiadmi.models.repository.ICategoriaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/categorias")
@Tag(
        name = "Categorías",
        description = "Endpoints para la gestión de categorías"
)
public class CategoriaController {

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @GetMapping
    @Operation(
            summary = "Obtener todas las categorías",
            description = "Devuelve una lista de categorías disponibles en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de categorías obtenido correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoriaDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "no encontrado"
            )
    })
    public ResponseEntity<?> obtenerColecciones(){

        try {

            List<CategoriaDTO> categorias =
                    categoriaRepository.findAll()
                            .stream()
                            .map(c -> new CategoriaDTO(c.getNombre()))
                            .toList();

            if (categorias.isEmpty()) {
                log.info("No se encontraron categorías");
            }

            return ResponseEntity.ok(categorias);


        } catch (Exception e) {


            log.error("Error consultando categorías", e);
            return ResponseEntity.status(500).body("Error interno del servidor");
        }

    }

}
