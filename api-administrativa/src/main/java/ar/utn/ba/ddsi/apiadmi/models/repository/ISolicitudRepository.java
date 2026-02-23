package ar.utn.ba.ddsi.apiadmi.models.repository;

import ar.utn.ba.ddsi.apiadmi.models.entities.admin.SolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISolicitudRepository extends JpaRepository<SolicitudEliminacion, Long> {

}
