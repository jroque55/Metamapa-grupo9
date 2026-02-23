package ar.utn.ba.ddsi.apiadmi.models.repository;

import ar.utn.ba.ddsi.apiadmi.models.entities.fuente.Fuente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFuentesRepository extends JpaRepository<Fuente, Long> {

    Fuente findByNombre(String nombre);
}
