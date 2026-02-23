package ar.utn.ba.ddsi.apipublica.models.repository;

import ar.utn.ba.ddsi.apipublica.models.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombreIgnoreCase(String nombre);
}

