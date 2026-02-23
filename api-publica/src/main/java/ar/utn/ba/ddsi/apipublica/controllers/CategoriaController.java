package ar.utn.ba.ddsi.apipublica.controllers;

import ar.utn.ba.ddsi.apipublica.models.dtos.CategoriaOutputDTO;
import ar.utn.ba.ddsi.apipublica.services.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/categorias")
@Tag(
        name = "Categorías",
        description = "Endpoints para la consulta de categorías"
)
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    @Operation(
            summary = "Listar categorías",
            description = "Devuelve un listado con todas las categorías disponibles"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de categorías obtenido correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = CategoriaOutputDTO.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<List<CategoriaOutputDTO>> listarCategorias() {
        log.info("Recibiendo solicitud para listar categorías");
        List<CategoriaOutputDTO> categorias = categoriaService.findAll();
        log.debug("Categorías obtenidas: {}", categorias.size());
        return ResponseEntity.ok(categorias);
    }


}
