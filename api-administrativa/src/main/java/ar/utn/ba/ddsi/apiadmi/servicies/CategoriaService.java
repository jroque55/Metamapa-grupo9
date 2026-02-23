package ar.utn.ba.ddsi.apiadmi.servicies;

import ar.utn.ba.ddsi.apiadmi.models.entities.hecho.Categoria;
import ar.utn.ba.ddsi.apiadmi.models.repository.ICategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.lang.Long.parseLong;

@Service
public class CategoriaService {
    @Autowired
    private ICategoriaRepository categorias;


    public Categoria buscarPorNombre(String nombre) {
        // Lógica para buscar y devolver la categoría por nombre
        return this.categorias.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada: " + nombre));

    }
    public Categoria buscarPorId(String id) {
        // Lógica para buscar y devolver la categoría por id
        Long identificator = parseLong(id);
        return this.categorias.findById(identificator)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada: " ));
    }

}
