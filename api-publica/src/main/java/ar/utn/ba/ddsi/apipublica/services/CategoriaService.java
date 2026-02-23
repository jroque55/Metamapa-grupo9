package ar.utn.ba.ddsi.apipublica.services;

import ar.utn.ba.ddsi.apipublica.models.dtos.CategoriaOutputDTO;
import ar.utn.ba.ddsi.apipublica.models.repository.CategoriaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<CategoriaOutputDTO> findAll() {
        log.info("Obteniendo todas las categorías");

        List<CategoriaOutputDTO> categorias = categoriaRepository.findAll().stream()
                .map(categoria -> new CategoriaOutputDTO(categoria.getNombre()))
                .collect(java.util.stream.Collectors.toList());

        log.debug("Categorías obtenidas: {}", categorias.size());
        return categorias;
    }

}
