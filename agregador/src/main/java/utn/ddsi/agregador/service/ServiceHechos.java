package utn.ddsi.agregador.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import utn.ddsi.agregador.domain.hecho.Hecho;
import utn.ddsi.agregador.dto.EstadisticaCantidadHoraCateDTO;
import utn.ddsi.agregador.dto.EstadisticaCategoriaDTO;
import utn.ddsi.agregador.dto.EstadisticaProviciaXCategoriaDTO;
import utn.ddsi.agregador.repository.IRepositoryHechos;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ServiceHechos {
    
    @Autowired
    private final IRepositoryHechos repositoryHechos;

    public ServiceHechos(IRepositoryHechos serviceHecho) {
        this.repositoryHechos = serviceHecho;
    }

    public Long contarHechosDeCategoria(Long id_categoria) {
        log.info("ServiceHechos: Contando hechos para la categoría ID: {}", id_categoria);
        Long cantidad = this.repositoryHechos.contarHechosDeCategoria(id_categoria);
        log.debug("Resultado del conteo para categoría {}: {}", id_categoria, cantidad);
        return cantidad;
    }

    public List<EstadisticaCategoriaDTO> contarHechosDeCategorias(){
        log.info("ServiceHechos: Solicitando estadísticas globales de todas las categorías");
        List<EstadisticaCategoriaDTO> resultados = this.repositoryHechos.contarHechosDeCategorias();

        if (resultados.isEmpty()) {
            log.warn("ServiceHechos: La consulta de estadísticas de categorías devolvió una lista vacía");
        } else {
            log.debug("ServiceHechos: Se recuperaron {} registros de categorías", resultados.size());
        }

        return resultados;
    }

    public List<EstadisticaProviciaXCategoriaDTO> obtenerCantidadDeHechosXProvinciaXCategoria(Long categoria) {
        log.info("ServiceHechos: Calculando hechos por provincia para la categoría ID: {}", categoria);
        List<EstadisticaProviciaXCategoriaDTO> rta = this.repositoryHechos.obtenerCantidadDeHechosXProvinciaXCategoria(categoria);

        if (rta.isEmpty()) {
            log.info("ServiceHechos: No se encontraron registros de provincias para la categoría ID: {}", categoria);
        }
        log.info("ServiceHechos: Encontró Resultados ");
        return rta;
    }

    public List<EstadisticaCantidadHoraCateDTO> obtenerCantidadDeHechosXDiaXCategoria(Long categoria) {
        log.info("ServiceHechos: Consultando distribución horaria para la categoría ID: {}", categoria);
        List<EstadisticaCantidadHoraCateDTO> rta = this.repositoryHechos.obtenerCantidadDeHechosXDiaXCategoria(categoria);

        if (rta.isEmpty()) {
            log.warn("ServiceHechos: No hay datos horarios registrados para la categoría ID: {}", categoria);
        } else {
            log.info("ServiceHechos: Análisis horario obtenido con {} franjas registradas", rta.size());
        }

        return rta;
    }

    /*

    public Hecho agregarHecho(Hecho hecho) {
        return repository.save(hecho);
    }

    public Optional<Hecho> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public List<Hecho> buscarPorTitulo(String titulo) {
        List<Hecho> hechos =repository.findAll();
        return hechos.stream()
                .filter(hecho -> hecho.getTitulo().equalsIgnoreCase(titulo))
                .toList();
    }

    public List<Hecho> obtenerTodos() {
        return repository.findAll();
    }

    public void eliminarHecho(Long id) {repository.deleteById(id);}
    */
    //Esto incluso podria ser un id (categoria)

}
