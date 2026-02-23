package ar.utn.ba.ddsi.apiadmi.models.repository;

import ar.utn.ba.ddsi.apiadmi.models.entities.hecho.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IHechosRepository extends JpaRepository<Hecho,Long> {
}
