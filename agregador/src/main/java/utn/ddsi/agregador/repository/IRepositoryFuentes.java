package utn.ddsi.agregador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import utn.ddsi.agregador.domain.fuentes.Fuente;
import utn.ddsi.agregador.utils.EnumTipoFuente;

import java.util.List;

public interface IRepositoryFuentes extends JpaRepository<Fuente, Long> {
    @Query("SELECT f FROM Coleccion c JOIN c.fuentes f WHERE c.id_coleccion = :idColeccion")
    List<Fuente> findFuentesByColeccion(Long idColeccion);

    Fuente findByUrl(String url);

    Fuente findFirstByTipoFuente(EnumTipoFuente enumTipoFuente);
}
