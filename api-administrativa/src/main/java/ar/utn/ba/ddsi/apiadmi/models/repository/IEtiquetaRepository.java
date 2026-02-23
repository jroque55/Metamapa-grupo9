package ar.utn.ba.ddsi.apiadmi.models.repository;

import ar.utn.ba.ddsi.apiadmi.models.entities.hecho.Categoria;
import ar.utn.ba.ddsi.apiadmi.models.entities.hecho.Etiqueta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IEtiquetaRepository  extends JpaRepository<Etiqueta,Long> {
    Optional<Categoria> findByNombre(String nombre);
    Optional<Etiqueta> findByNombreIgnoreCase(String nombre);
}
