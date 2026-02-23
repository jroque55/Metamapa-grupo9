package ar.utn.ba.ddsi.apipublica.controllers;

import ar.utn.ba.ddsi.apipublica.models.dtos.HechoCreateDTO;
import ar.utn.ba.ddsi.apipublica.models.dtos.HechoFilterDTO;
import ar.utn.ba.ddsi.apipublica.models.dtos.HechoOutputDTO;
import ar.utn.ba.ddsi.apipublica.models.entities.Hecho;
import ar.utn.ba.ddsi.apipublica.services.IHechoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/hechos")
@Tag(
        name = "Hechos",
        description = "Endpoints para la creación y consulta de hechos"
)
public class HechoController {

    private final IHechoService hechoService;

    public HechoController(IHechoService hechoService) {
        this.hechoService = hechoService;
    }

    @PostMapping//ResponseEntity???
    @Operation(
            summary = "Crear un hecho",
            description = "Crea un nuevo hecho en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Hecho creado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Hecho.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos (el título es obligatorio)"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<?> crearHecho(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para crear un hecho",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HechoCreateDTO.class)
                    )
            )
            @Valid @RequestBody HechoCreateDTO dto) {
        if (dto == null || dto.getTitulo() == null || dto.getTitulo().isBlank()) {
            return ResponseEntity.badRequest().body("Titulo es obligatorio");
        }

            Hecho hecho = hechoService.crearHecho(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(hecho);

    }

    //GET /hechos con filtros por query params
    @GetMapping
    @Operation(
            summary = "Buscar hechos",
            description = "Devuelve un listado paginado de hechos aplicando filtros opcionales"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de hechos obtenido correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HechoOutputDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetros de búsqueda inválidos"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<?> listarHechosSegun(
            @Parameter(description = "Categoría del hecho", example = "Política")
            @RequestParam(value = "categoria", required = false) String categoria,
            @Parameter(description = "Fecha de reporte desde (YYYY-MM-DD)", example = "2024-01-01")
            @RequestParam(value = "fecha_reporte_desde", required = false) String fechaReporteDesde,
            @Parameter(description = "Fecha de reporte hasta (YYYY-MM-DD)", example = "2024-12-31")
            @RequestParam(value = "fecha_reporte_hasta", required = false) String fechaReporteHasta,
            @Parameter(description = "Fecha de acontecimiento desde", example = "2023-01-01")
            @RequestParam(value = "fecha_acontecimiento_desde", required = false) String fechaAcontecimientoDesde,
            @Parameter(description = "Fecha de acontecimiento hasta", example = "2023-12-31")
            @RequestParam(value = "fecha_acontecimiento_hasta", required = false) String fechaAcontecimientoHasta,
            @Parameter(description = "Provincia del hecho", example = "Córdoba")
            @RequestParam(value = "provincia", required = false) String provincia,
            @Parameter(description = "Texto libre (título, descripción, fuente)", example = "elecciones")
            @RequestParam(value = "q", required = false) String textoLibre, //Titulo,Descripcion,Fuente, esasa cosas son las que busca el texto libre
            @Parameter(description = "Tipo de fuente", example = "OFICIAL")
            @RequestParam(value = "tipoFuente", required=false) String tipoFuente,
            @Parameter(description = "Número de página", example = "0")
            @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página", example = "10")
            @RequestParam(value = "size", defaultValue = "10") int size)
    {
        log.info("Obtener hechos con filtros ");
        HechoFilterDTO filter = new HechoFilterDTO(categoria, fechaReporteDesde, fechaReporteHasta,
                fechaAcontecimientoDesde, fechaAcontecimientoHasta, provincia, textoLibre,tipoFuente);
        System.out.println(filter.getCategoria());
       log.info("Empezando busqueda filtrada ");

        // Validaciones de paginacion para evitar requests abusivos
        if (page < 0) page = 0;
        if (size <= 0) size = 10;
        if (size > 100) size = 100; // tope maximo razonable

        log.debug("Listar hechos page={} size={}", page, size);

             Page<HechoOutputDTO> resultados = hechoService.buscarConFiltro(filter, page, size);
             return ResponseEntity.status(HttpStatus.OK).body(resultados);

    }
    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener hecho por ID",
            description = "Devuelve un hecho específico a partir de su identificador"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Hecho encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HechoOutputDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Hecho no encontrado"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<Object> obtenerHecho(
            @Parameter(description = "ID del hecho", required = true)
            @NotNull @PathVariable("id") Long id) {

            log.info("Intentando obtener el hecho con ID: {}", id);
            HechoOutputDTO hechoDTO = hechoService.obtenerHechoPorId(id);
            log.info("Hecho obtenido correctamente de la BD, intentando enviar al frontend...");
            return ResponseEntity.status(HttpStatus.OK).body(hechoDTO);
    }
}
