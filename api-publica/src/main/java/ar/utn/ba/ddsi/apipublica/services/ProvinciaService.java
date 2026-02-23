package ar.utn.ba.ddsi.apipublica.services;

import ar.utn.ba.ddsi.apipublica.models.dtos.ProvinciaOutputDTO;
import ar.utn.ba.ddsi.apipublica.models.repository.ProvinciaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class ProvinciaService {
    private final ProvinciaRepository provinciaRepository;

    public ProvinciaService(ProvinciaRepository provinciaRepository) {
        this.provinciaRepository = provinciaRepository;
    }

    public List<ProvinciaOutputDTO> findAll() {
        log.info("Obteniendo todas las provincias");

        List<ProvinciaOutputDTO> provincias = provinciaRepository.findAll().stream()
                .map(provincia -> new ProvinciaOutputDTO(provincia.getNombre(), provincia.getPais()))
                .toList();

        log.debug("Provincias obtenidas: {}", provincias.size());
        return provincias;
    }

}
