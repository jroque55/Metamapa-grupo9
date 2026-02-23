package utn.ddsi.agregador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utn.ddsi.agregador.domain.coleccion.Coleccion;
import utn.ddsi.agregador.domain.condicion.InterfaceCondicion;
import utn.ddsi.agregador.domain.hecho.Hecho;

import java.util.List;
import java.util.Optional;

@Repository
public interface IRepositoryColecciones extends JpaRepository<Coleccion, Long> {
    Coleccion findByTitulo(String nombre);

    @Query("SELECT DISTINCT c.titulo FROM Coleccion c")
    List<String> obtenerNombresColecciones();

    @Query("SELECT cond FROM Coleccion c JOIN c.condicionDePertenencia cond WHERE c.id = :id")
    List<InterfaceCondicion> findByIdCondiciones(@Param("id") Long id);


    @Query("SELECT c.id_coleccion FROM Coleccion c")
    List<Long> findAllIds();
}
