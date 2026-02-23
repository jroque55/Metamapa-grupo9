package ar.utn.ba.ddsi.apipublica.models.repository;

import ar.utn.ba.ddsi.apipublica.models.entities.Fuente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FuenteRepository extends JpaRepository<Fuente, Long> {
    Optional<Fuente> findById(long idFuente);
    Optional<Fuente> findByNombreIgnoreCase(String nombre);
}
