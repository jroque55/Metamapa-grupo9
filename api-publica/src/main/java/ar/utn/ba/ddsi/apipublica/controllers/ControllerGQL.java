package ar.utn.ba.ddsi.apipublica.controllers;

import ar.utn.ba.ddsi.apipublica.models.dtos.*;
import ar.utn.ba.ddsi.apipublica.services.CategoriaService;
import ar.utn.ba.ddsi.apipublica.services.ColeccionService;
import ar.utn.ba.ddsi.apipublica.services.IHechoService;
import ar.utn.ba.ddsi.apipublica.services.ProvinciaService;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class ControllerGQL {
    private final IHechoService hechoService;
    private final ColeccionService coleccionService;
    private final CategoriaService categoriaService;
    private final ProvinciaService provinciaService;

    public ControllerGQL(IHechoService hechoService, ColeccionService coleccionService, CategoriaService categoriaService, ProvinciaService provinciaService) {
        this.hechoService = hechoService;
        this.coleccionService = coleccionService;
        this.categoriaService = categoriaService;
        this.provinciaService = provinciaService;
    }

    @QueryMapping
    public List<CategoriaOutputDTO> listarCategorias(){
        return this.categoriaService.findAll();
    }

    @QueryMapping
    public List<ProvinciaOutputDTO> listarProvincias(){
        return this.provinciaService.findAll();
    }

    @QueryMapping
    public ColeccionOutputDTO listarHechosDeUnaColeccion(
            @Argument Long coleccionID,
            @Argument String modoNavegacion,
            @Argument String categoria,
            @Argument String fecha_reporte_desde,
            @Argument String fecha_reporte_hasta,
            @Argument String fecha_acontecimiento_desde,
            @Argument String fecha_acontecimiento_hasta,
            @Argument String provincia,
            @Argument String q,
            @Argument String fuenteTipo
    ){
        HechoFilterDTO filter = new HechoFilterDTO(
                categoria,
                fecha_reporte_desde,
                fecha_reporte_hasta,
                fecha_acontecimiento_desde,
                fecha_acontecimiento_hasta,
                provincia,
                q,
                fuenteTipo
        );
        ColeccionOutputDTO coleccion = coleccionService.buscarColeccionPorId(coleccionID);
        List<HechoOutputDTO> hechos = coleccionService.buscarHechosSegun(filter, modoNavegacion, coleccionID);
        coleccion.setHechos(hechos);
        return coleccion;
    }

    @QueryMapping
    public List<ColeccionOutputDTO> listarColecciones(
            @Argument String titulo,
            @Argument String descripcion,
            @Argument String tipo_algoritmo,
            @Argument List<String> fuente_id
    ) {
        ColeccionFilterDTO filter = new ColeccionFilterDTO(titulo, descripcion, tipo_algoritmo, fuente_id);
        return this.coleccionService.buscarColeccionesSegun(filter);
    }

    @QueryMapping
    public ColeccionOutputDTO obtenerColeccionPorId(
            @Argument Long coleccionID) {
        return this.coleccionService.buscarColeccionPorId(coleccionID);
    }

    @QueryMapping
    public Page<HechoOutputDTO> listarHechosSegun(
            @Argument String categoria,
            @Argument String fecha_reporte_desde,
            @Argument String fecha_reporte_hasta,
            @Argument String fecha_acontecimiento_desde,
            @Argument String fecha_acontecimiento_hasta,
            @Argument String provincia,
            @Argument String q,
            @Argument String fuenteTipo,
            @Argument Integer page,
            @Argument Integer size
    ) {
        HechoFilterDTO filter = new HechoFilterDTO(
                categoria, fecha_reporte_desde, fecha_reporte_hasta,
                fecha_acontecimiento_desde, fecha_acontecimiento_hasta,
                provincia, q, fuenteTipo
        );
        return hechoService.buscarConFiltro(filter, page, size);
    }

    @QueryMapping
    public HechoOutputDTO obtenerHecho(@Argument Long id) {
        return hechoService.obtenerHechoPorId(id);
    }
}
