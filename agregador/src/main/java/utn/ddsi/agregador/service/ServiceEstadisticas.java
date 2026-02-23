package utn.ddsi.agregador.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.ddsi.agregador.domain.coleccion.Coleccion;
import utn.ddsi.agregador.domain.hecho.Categoria;
import utn.ddsi.agregador.dto.*;
import utn.ddsi.agregador.repository.IRepositoryCategorias;
import utn.ddsi.agregador.repository.IRepositoryHechoXColeccion;
import utn.ddsi.agregador.repository.IRepositoryProvincias;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ServiceEstadisticas {

    @Autowired
    private ServiceColecciones serviceColecciones;
    private ServiceHechos serviceHechos;
    private IRepositoryHechoXColeccion repoHechosXColeccion;
    private ServiceSolicitudes serviceSolicitudes;
    private IRepositoryCategorias repoCategorias;
    private IRepositoryProvincias repoProvincias;
    
    public ServiceEstadisticas(ServiceHechos serviceHechos, ServiceColecciones serviceColecciones,ServiceSolicitudes serviceSolicitudes,IRepositoryHechoXColeccion repoHXC, IRepositoryCategorias repoCategorias,IRepositoryProvincias repoProvincias) {
        this.serviceHechos= serviceHechos;
        this.serviceColecciones = serviceColecciones;
        this.serviceHechos = serviceHechos;
        this.repoHechosXColeccion = repoHXC;
        this.serviceSolicitudes = serviceSolicitudes;
        this.repoCategorias = repoCategorias;
        this.repoProvincias= repoProvincias;
    }

    // METODOS PARA OBTENER NOMBRES NECESARIOS PARA LA ESTADISTICA
    public List<String> obtenerNombreColecciones() {
        log.info("Estadísticas: Solicitando lista de nombres de colecciones");
        return this.serviceColecciones.obtenerNombreDeColecciones();
    }

    public List<String> obtenerNombreProvincias() {
        log.info("Estadísticas: Solicitando lista de nombres de provincias");
        return this.repoProvincias.obtenerNombreDeProvincias();
    }

    public List<String> obtenerNombreCategorias() {
        log.info("Estadísticas: Solicitando lista de nombres de categorías");
        return this.repoCategorias.obtenerNombreDeCategorias();
    }

    // De una coleccion, en que provincia se agrupan la mayor cantidad de hechos reportados?
    public List<EstadisticaColeccionHechosXProvinciaDTO> obtenerCantidadHechosDeColeccion(String nombreColeccion) {
        log.info("Calculando cantidad de hechos por provincia para la colección: '{}'", nombreColeccion);

        Coleccion coleccionBuscada = this.serviceColecciones.obtenerPorNombre(nombreColeccion);

        if (coleccionBuscada == null) {
            log.warn("No se pudo calcular estadística: La colección '{}' no existe", nombreColeccion);
            return null;
        }

        Long id_coleccion = coleccionBuscada.getId_coleccion();
        List<EstadisticaColeccionHechosXProvinciaDTO> estadistica = this.repoHechosXColeccion.contarHechosDeColeccionDeProvincia(id_coleccion);

        if (estadistica.isEmpty()) {
            log.info("No se encontraron hechos registrados para la colección ID: {}", id_coleccion);
            return null;
        }

        log.debug("Estadística generada con {} registros de provincias", estadistica.size());
        return estadistica;
    }

    public List<EstadisticaCategoriaDTO> obtenerCantidadDeHechosXCategoria() {
        log.info("Generando estadística general de hechos por categoría");
        List<EstadisticaCategoriaDTO> resultado = this.serviceHechos.contarHechosDeCategorias();

        if (resultado.isEmpty()) {
            log.warn("La estadística de hechos por categoría no devolvió datos");
        } else {
            log.info("Estadística de categorías obtenida exitosamente");
        }

        return resultado;
    }

    // En que provincia se presenta la mayor cantidad de hechos de una cierta categoria?
    public List<EstadisticaProviciaXCategoriaDTO> obtenerCantidadDeHechoXProvinciaXCategoria(String categoria) {
        log.info("Iniciando cálculo de hechos por provincia para la categoría: '{}'", categoria);

        Categoria cate = this.repoCategorias.findByNombre(categoria);
        if (cate == null) {
            log.warn("Categoría '{}' no encontrada. No se puede procesar estadística.", categoria);
            return new ArrayList<>();
        }

        List<EstadisticaProviciaXCategoriaDTO> rta = this.serviceHechos.obtenerCantidadDeHechosXProvinciaXCategoria(cate.getId_categoria());
        log.debug("Se obtuvieron {} registros para la estadística provincia-categoría", rta.size());
        return rta;
    }

    // A que hora del dia ocurren la mayor cantidad de hechos de una cierta categoria?
    public List<EstadisticaCantidadHoraCateDTO> obtenerCantidadDeHechosXHoraXCategoria(String categoria) {
        log.info("Analizando Cantidad de hechos subidos según la hora para la categoría: '{}'", categoria);

        Categoria cate = this.repoCategorias.findByNombre(categoria);
        if (cate == null) {
            log.warn("No se encontró la categoría '{}' para el análisis horario", categoria);
            return new ArrayList<>();
        }

        List<EstadisticaCantidadHoraCateDTO> rta = this.serviceHechos.obtenerCantidadDeHechosXDiaXCategoria(cate.getId_categoria());
        log.info("Análisis horario completado para categoría ID: {}", cate.getId_categoria());
        return rta;
    }

    public EstadisticaSolicitudesDTO obtenerCantidadSpamEnSolicitudes() {
        log.info("Consultando estadísticas de SPAM en solicitudes");
        EstadisticaSolicitudesDTO dto = this.serviceSolicitudes.cantidadSolicitudesSpam();

        if (dto != null) {
            log.debug("Datos de spam recuperados correctamente");
        }

        return dto;
    }
}