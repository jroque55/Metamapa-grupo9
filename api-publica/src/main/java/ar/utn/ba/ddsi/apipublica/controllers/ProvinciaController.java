package ar.utn.ba.ddsi.apipublica.controllers;
import ar.utn.ba.ddsi.apipublica.models.dtos.ProvinciaOutputDTO;
import ar.utn.ba.ddsi.apipublica.services.ProvinciaService;
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
@RequestMapping("/provincias")
@Tag(
        name = "Provincias",
        description = "Endpoints para la consulta de provincias"
)
public class ProvinciaController {

    private final ProvinciaService provinciaService;

    public ProvinciaController(ProvinciaService provinciaService) {
        this.provinciaService = provinciaService;
    }

    @GetMapping
    @Operation(
            summary = "Listar provincias",
            description = "Devuelve un listado con todas las provincias disponibles"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de provincias obtenido correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ProvinciaOutputDTO.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<List<ProvinciaOutputDTO>> listarProvincias() {
        List<ProvinciaOutputDTO> provincias = provinciaService.findAll();
        return ResponseEntity.ok(provincias);
    }
}


