package utn.ddsi.agregador.controller;

import jakarta.websocket.server.PathParam;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchTransactionManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utn.ddsi.agregador.domain.coleccion.Coleccion;
import utn.ddsi.agregador.domain.hecho.Hecho;
import utn.ddsi.agregador.dto.*;
import utn.ddsi.agregador.service.ServiceEstadisticas;
import utn.ddsi.agregador.service.ServiceHechos;

import java.util.List;
import utn.ddsi.agregador.domain.hecho.Categoria;
import utn.ddsi.agregador.domain.hecho.Ubicacion;
import utn.ddsi.agregador.domain.fuentes.Fuente;
import utn.ddsi.agregador.utils.EnumTipoFuente;

import javax.print.DocFlavor;
import java.time.LocalDate;
import java.util.ArrayList;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@RestController
@RequestMapping("/estadisticas")
@Tag(
        name = "Estadísticas",
        description = "Consultas estadísticas sobre hechos, colecciones, categorías y solicitudes"
)
public class ControllerEstadisticas {

    @Autowired
    private ServiceEstadisticas service;

    public ControllerEstadisticas(ServiceEstadisticas service) {
        this.service = service;
    }

    @Operation(
            summary = "Cantidad de hechos por provincia en una colección",
            description = "Obtiene la cantidad de hechos agrupados por provincia para una colección determinada"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estadísticas obtenidas correctamente",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = EstadisticaColeccionHechosXProvinciaDTO.class)
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Parámetro de colección inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/provinciaxcol")
    public List<EstadisticaColeccionHechosXProvinciaDTO> obtenerCantidadDeHechosXProvincia(
            @Parameter(
                    description = "titulo de la coleccion",
                    example = "Colección Ambiental",
                    required = true
            )
            @RequestParam(value = "coleccion") String nombreColeccion
    ) {
        log.info("CONTROLLER : Obtener cantidad de hechos por provincia para coleccion: {}", nombreColeccion);
        return this.service.obtenerCantidadHechosDeColeccion(nombreColeccion);
    }

    @Operation(
            summary = "Cantidad de hechos por categoría",
            description = "Obtiene la cantidad total de hechos agrupados por categoría"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estadísticas obtenidas correctamente",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = EstadisticaCategoriaDTO.class)
                            )
                    )
            )
    })
    @GetMapping("/categoria")
    public List<EstadisticaCategoriaDTO> obtenerCantidadDeHechosXCategoria() {
        log.info("CONTROLLER : Obtener cantidad de hechos por categoria");
        return this.service.obtenerCantidadDeHechosXCategoria();
    }

    @Operation(
            summary = "Cantidad de hechos por provincia y categoría",
            description = "Obtiene la cantidad de hechos agrupados por provincia para una categoría específica"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estadística obtenida correctamente",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = EstadisticaProviciaXCategoriaDTO.class)
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Categoría inválida")
    })
    @GetMapping("/provinciaxcat")
    public List<EstadisticaProviciaXCategoriaDTO> obtenerCantidadDeHechoXProvinciaXCategoria(
            @Parameter(
                    description = "Nombre de la categoría",
                    example = "Incendios",
                    required = true
            )
            @RequestParam(value = "categoria") String categoria
    ) {
        log.info("CONTROLLER : Obtener cantidad de hechos por provincia y categoria: {}", categoria);
        return this.service.obtenerCantidadDeHechoXProvinciaXCategoria(categoria);
    }

    @Operation(
            summary = "Cantidad de hechos por hora del día y categoría",
            description = "Obtiene la cantidad de hechos ocurridos por hora del día para una categoría específica\n"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estadística obtenida correctamente",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = EstadisticaCantidadHoraCateDTO.class)
                            )
                    )
            )
    })
    @GetMapping("/hora")
    public List<EstadisticaCantidadHoraCateDTO> obtenerCantidadDeHechosXHoraXCategoria(
            @Parameter(
                    description = "Nombre de la categoría",
                    example = "Accidentes",
                    required = true
            )
            @NonNull @RequestParam(value = "categoria") String categoria
    ) {
        log.info("CONTROLLER : Obtener cantidad de hechos por hora y categoria: {}", categoria);
        return this.service.obtenerCantidadDeHechosXHoraXCategoria(categoria);
    }

    @Operation(
            summary = "Cantidad de solicitudes de eliminación consideradas spam",
            description = "Obtiene estadísticas sobre solicitudes de eliminación marcadas como spam\n"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estadística obtenida correctamente",
                    content = @Content(
                            schema = @Schema(implementation = EstadisticaSolicitudesDTO.class)
                    )
            )
    })
    @GetMapping("/solicitudesSpam")
    public EstadisticaSolicitudesDTO obtenerCantidadSpamEnSolicitudes() {
        log.info("CONTROLLER : Obtener cantidad de solicitudes spam");
        return this.service.obtenerCantidadSpamEnSolicitudes();
    }

    @Operation(summary = "Obtener nombres de colecciones")
    @GetMapping("/colecciones/nombre")
    public List<String> obtenerColecciones() {
        return this.service.obtenerNombreColecciones();
    }

    @Operation(summary = "Obtener nombres de provincias")
    @GetMapping("/provincias/nombre")
    public List<String> obtenerProvincias() {
        return this.service.obtenerNombreProvincias();
    }

    @Operation(summary = "Obtener nombres de categorías")
    @GetMapping("/categorias/nombre")
    public List<String> obtnerCategorias() {
        return this.service.obtenerNombreCategorias();
    }
}
