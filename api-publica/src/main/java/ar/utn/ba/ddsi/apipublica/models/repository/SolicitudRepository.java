package ar.utn.ba.ddsi.apipublica.models.repository;

import ar.utn.ba.ddsi.apipublica.models.entities.SolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudRepository extends JpaRepository<SolicitudEliminacion, Long> {
}
