package utn.ddsi.agregador.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import utn.ddsi.agregador.dto.*;
import utn.ddsi.agregador.service.ServiceEstadisticas;

import java.util.List;

@Controller
public class ControllerEstadisticasGQL {

    private final ServiceEstadisticas service;

    @Autowired // Opcional si es el único constructor
    public ControllerEstadisticasGQL(ServiceEstadisticas service) {
        this.service = service;
    }

    @QueryMapping
    public List<EstadisticaColeccionHechosXProvinciaDTO> obtenerCantidadDeHechosXProvincia(@Argument String coleccion) {
        return this.service.obtenerCantidadHechosDeColeccion(coleccion);
    }

    @QueryMapping
    public List<EstadisticaCategoriaDTO> obtenerCantidadDeHechosXCategoria() {
        return this.service.obtenerCantidadDeHechosXCategoria();
    }

    @QueryMapping
    public List<EstadisticaProviciaXCategoriaDTO> obtenerCantidadDeHechoXProvinciaXCategoria(@Argument String categoria) {
        return this.service.obtenerCantidadDeHechoXProvinciaXCategoria(categoria);
    }

    @QueryMapping
    public List<EstadisticaCantidadHoraCateDTO> obtenerCantidadDeHechosXHoraXCategoria(@Argument String categoria) {
        return this.service.obtenerCantidadDeHechosXHoraXCategoria(categoria);
    }

    @QueryMapping
    public EstadisticaSolicitudesDTO obtenerCantidadSpamEnSolicitudes() {
        return this.service.obtenerCantidadSpamEnSolicitudes();
    }

    @QueryMapping
    public List<String> obtenerColecciones() {
        return this.service.obtenerNombreColecciones();
    }

    @QueryMapping
    public List<String> obtenerProvincias() {
        return this.service.obtenerNombreProvincias();
    }

    @QueryMapping
    public List<String> obtenerCategorias() {
        return this.service.obtenerNombreCategorias();
    }

}
