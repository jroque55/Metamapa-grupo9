package utn.ddsi.agregador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import utn.ddsi.agregador.domain.hecho.Provincia;

import java.util.List;

@Repository
public interface IRepositoryProvincias extends JpaRepository<Provincia,Long> {
    @Query("SELECT DISTINCT p.nombre FROM Provincia p")
    List<String> obtenerNombreDeProvincias();

    Provincia findFirstByNombre(String nombre);
}
