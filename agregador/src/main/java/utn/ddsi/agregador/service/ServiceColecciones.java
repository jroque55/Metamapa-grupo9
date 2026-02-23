package utn.ddsi.agregador.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import utn.ddsi.agregador.domain.coleccion.Coleccion;
import utn.ddsi.agregador.repository.IRepositoryColecciones;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ServiceColecciones {

    @Autowired
    private IRepositoryColecciones repositoryColecciones;
//    /* PREGUNTA SERIA, ESTO SIRVE? para mi no atte Fabri
//    public void cargarColeccionConHechos(Long id, List<Hecho> hechos){
//        Optional<Coleccion> coleccionConTitulo = repositoryColecciones.findById(id);
//        if(coleccionConTitulo.isPresent()){
//            coleccionConTitulo.get().agregarHechos(hechos);
//        }
//        else{
//            throw new RuntimeException("no se encontró la coleccion");
//        }
//    }*/
//    public void crearColeccion(String titulo, String descripcion) {
//        Coleccion nuevaCole = new Coleccion(titulo,descripcion);
//        repositoryColecciones.save(nuevaCole);
//    }


    public List<Coleccion> obtenerTodasLasColecciones(){
        return repositoryColecciones.findAll();
    }
    //No se por que est definido asi jaja (att:yeri)
    public List<Coleccion> buscarPorID(Long id) {
        log.info("Buscando por ID {}", id);
        List<Coleccion> colecciones = repositoryColecciones.findAll();
        log.debug("Se recuperaron {} registros totaes de la base de datos", colecciones.size());
        List<Coleccion> rta = new ArrayList<>();
        colecciones.forEach(coleccion -> {
            if(coleccion.getId_coleccion().equals(id)) rta.add(coleccion);
        });
        if (rta.isEmpty()) {
            log.warn("No se encontraron coincidencias para el ID: {}", id);
        } else {
            log.info("Búsqueda finalizada. Coincidencias encontradas: {}", rta.size());
        }
        return rta;
    }
    public void eliminarColeccion(Long idColeccion) {
        log.info("Intentando eliminar la colección con ID: {}", idColeccion);
        repositoryColecciones.deleteById(idColeccion);
        log.info("Proceso de eliminación finalizado para el ID: {}", idColeccion);
    }

    public Coleccion obtenerPorNombre(String nombre) {
        log.info("Iniciando búsqueda de colección por nombre: '{}'", nombre);
        Coleccion resultado = this.repositoryColecciones.findByTitulo(nombre);

        if (resultado == null) {
            log.warn("No se encontró ninguna colección con el nombre: '{}'", nombre);
        } else {
            log.info("Colección encontrada exitosamente, Id: {}", resultado.getId_coleccion());
        }

        return resultado;
    }

    public List<String> obtenerNombreDeColecciones() {
        log.info("Solicitando lista completa de nombres de colecciones");
        List<String> nombres = this.repositoryColecciones.obtenerNombresColecciones();

        if (nombres.isEmpty()) {
            log.info("La consulta de nombres devolvió una lista vacía");
        } else {
            log.debug("Se recuperaron {} nombres de colecciones", nombres.size());
        }

        return nombres;
    }

    public void agregar(Coleccion coleccion) {
        log.info("Intentando agregar una nueva colección");
        if (coleccion == null) {
            log.error("Fallo al agregar: el objeto Coleccion proporcionado es null");
            throw new IllegalArgumentException("Coleccion es null");
        }

        repositoryColecciones.save(coleccion);
        // Usamos el ID para confirmar la persistencia en el log
        if(coleccion.getId_coleccion()!=null){
            log.info("ServiceColecciones: Coleccion guardada exitosamente con ID: {}", coleccion.getId_coleccion());
        }else{
            log.warn("ServiceColecciones: La coleccion no pudo ser guardada");
        }
    }
}
