package utn.ddsi.agregador.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utn.ddsi.agregador.domain.coleccion.HechoXColeccion;
import utn.ddsi.agregador.domain.hecho.Hecho;
import utn.ddsi.agregador.dto.EstadisticaColeccionHechosXProvinciaDTO;
import utn.ddsi.agregador.dto.MencionDeHecho;

import java.util.List;

@Repository
public interface IRepositoryHechoXColeccion extends JpaRepository<HechoXColeccion,Long> {

    /*@Query("SELECT  pro.nombre ,count(distinct h.id_hecho)  FROM HechoXColeccion hc " +
            "JOIN Hecho h" +
            "JOIN Ubicacion u" +
            "JOIN Provincia pro" +
            "where hc.id_coleccion = coleccion_Id" +
            "group by pro.nombre ")
    */
    @Query("SELECT new utn.ddsi.agregador.dto.EstadisticaColeccionHechosXProvinciaDTO(" + // <--- RUTA COMPLETA
            "u.provincia.nombre, COUNT(h)) " +
            "FROM HechoXColeccion hc " +
            "JOIN hc.hecho h " +
            "JOIN h.ubicacion u " +
            "WHERE hc.coleccion.id_coleccion = :coleccion_Id " +
            "GROUP BY u.provincia.nombre")
    List<EstadisticaColeccionHechosXProvinciaDTO> contarHechosDeColeccionDeProvincia(@Param("coleccion_Id") Long coleccioId);


    @Query("SELECT hc FROM HechoXColeccion hc WHERE hc.coleccion.id_coleccion = :coleccionId")
    List<HechoXColeccion> findByColeccion(Long coleccionId);

    @Query("SELECT hc FROM HechoXColeccion hc where hc.coleccion.id_coleccion = :coleccionId and hc.hecho.id_hecho = :hechoId")
    List<HechoXColeccion> findByConjunto(Long coleccionId, Long hechoId);

    @Query("""
        SELECT new utn.ddsi.agregador.dto.MencionDeHecho(
        hxc.hecho.id_hecho,
        hxc.hecho.fuente.id_fuente,
        hxc.hecho.descripcion
    )
    FROM HechoXColeccion hxc
    WHERE hxc.coleccion.id_coleccion = :idColeccion
    """)
    List<MencionDeHecho> findMencionesDeHechos(@Param("idColeccion") Long idColeccion);
    /*
    @Query(""+
    "SELECT hxc "+
    "FROM HechoXColeccion hxc "+
    "JOIN hxc.hecho.fuente " +
    "WHERE hxc.coleccion.id_coleccion = :idCol "+
    "GROUP BY hxc.id_hecho_x_coleccion")
    List<HechoXColeccion> findByColeccionOptimizado(Long idCol);*/
}


