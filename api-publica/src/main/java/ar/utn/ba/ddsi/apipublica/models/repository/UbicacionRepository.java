package ar.utn.ba.ddsi.apipublica.models.repository;

import ar.utn.ba.ddsi.apipublica.models.entities.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
}

