package ar.utn.ba.ddsi.fuenteproxy.models.repository.imp;


import ar.utn.ba.ddsi.fuenteproxy.models.entities.Fuente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FuenteRepository extends JpaRepository<Fuente, Long> {
    @Query("SELECT f FROM Fuente f WHERE f.id_fuente > :ultimoID")
    List<Fuente> findByIdFuenteGreaterThan(Long ultimoID);
}