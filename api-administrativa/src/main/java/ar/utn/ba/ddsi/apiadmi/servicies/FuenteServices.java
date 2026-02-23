package ar.utn.ba.ddsi.apiadmi.servicies;

import ar.utn.ba.ddsi.apiadmi.models.entities.fuente.Fuente;
import ar.utn.ba.ddsi.apiadmi.models.repository.IFuentesRepository;
import ar.utn.ba.ddsi.apiadmi.servicies.interfaces.IFuenteServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class FuenteServices implements IFuenteServices {

    @Autowired
    private IFuentesRepository repoFuentes;

    /*@Override
    public void agregarFuenteDeColeccion( idFuente) {
        this.repoFuentes.save(idFuente);
    }
*/

    @Override
    public Fuente buscarPorId(Long id) {
        log.debug("Buscando fuente de id {}", id);
        Fuente fuente = this.repoFuentes.findById(id)
                .orElseThrow(() -> new RuntimeException("Colección no encontrada"));

        return fuente;
    }
    @Override
    public Fuente buscarPorNombre(String nombre) {
        log.debug("Buscando fuente: {}", nombre);
        Fuente fuente = this.repoFuentes.findByNombre(nombre);
        if (fuente == null) {
            log.error("Fuente no encontrada: {}", nombre);
            throw new RuntimeException("Fuente no encontrada");
        }
        return fuente;
    }

    @Override
    public List<Fuente> obtenerFuentes() {

        List<Fuente> fuentes = this.repoFuentes.findAll();
        if(fuentes.isEmpty()){
            log.warn("No se encontraron fuentes en la base de datos");
        } else {
            log.debug("Fuentes obtenidas: {}", fuentes.size());
        }
        return fuentes;
    }

}
