package ar.utn.ba.ddsi.apiadmi.servicies;

import ar.utn.ba.ddsi.apiadmi.models.entities.hecho.Etiqueta;
import ar.utn.ba.ddsi.apiadmi.models.repository.IEtiquetaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.Long.parseLong;
@Slf4j
@Service
public class EtiquetaService {
    @Autowired
    private IEtiquetaRepository etiquetaRepository;

    public Etiqueta buscarPorId(String id) {
        log.info("Obteniendo etiqueta con ID: {}", id);
        Long identificator = parseLong(id);
        return this.etiquetaRepository.findById(identificator).orElseThrow(() ->{
                log.error("Etiqueta con ID {} no encontrada", id);
                return new RuntimeException("Etiqueta no encontrada: " + id);});

    }

    public List<Etiqueta> obtenerTodas() {
        log.info("Solicitando listado de todas las etiquetas desde base de datos");

        List<Etiqueta> etiquetas = etiquetaRepository.findAll();

        log.info("Se obtuvieron {} etiquetas", etiquetas.size());

        return etiquetas;
    }

}
