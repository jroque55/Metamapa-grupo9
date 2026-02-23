package ar.utn.ba.ddsi.apipublica.models.repository;


import ar.utn.ba.ddsi.apipublica.models.entities.Coleccion;
import ar.utn.ba.ddsi.apipublica.models.entities.Hecho;
import ar.utn.ba.ddsi.apipublica.models.entities.EnumTipoDeAlgoritmo;
import ar.utn.ba.ddsi.apipublica.models.entities.EnumEstadoHecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface ColeccionRepository extends JpaRepository<Coleccion,Long> {
    //Revisar Consulta
    @Query("SELECT hxc.hecho FROM HechoXColeccion hxc " +
            "JOIN hxc.hecho h " +
            "WHERE hxc.coleccion.id_coleccion = :coleccionId " +
            "AND (h.estado  IS NULL OR h.estado <> ar.utn.ba.ddsi.apipublica.models.entities.EnumEstadoHecho.BAJA) "+            // Para coordenadas: si ambas son NULL entonces no filtra; si no, compara con un rango (ej. ~1km -> 0.01 grados aprox)
            "AND (:curado IS NULL OR hxc.consensuado = :curado) " +
            "AND (:categoriaNombre IS NULL OR LOWER(h.categoria.nombre) = LOWER(:categoriaNombre)) " +
            "AND (:repDesde IS NULL OR h.fechaDeCarga >= :repDesde) " +
            "AND (:repHasta IS NULL OR h.fechaDeCarga <= :repHasta) " +
            "AND (:acaDesde IS NULL OR h.fecha >= :acaDesde) " +
            "AND (:acaHasta IS NULL OR h.fecha <= :acaHasta) " +
            "AND (:provincia IS NULL OR (h.ubicacion IS NOT NULL AND LOWER(h.ubicacion.provincia.nombre) = LOWER(:provincia))) " +
            "AND (:fuenteTipo IS NULL OR h.fuente IS NOT NULL AND STR(h.fuente.tipoFuente) = :fuenteTipo) " +
            "AND (:texto IS NULL OR (" +
                "LOWER(h.titulo) LIKE LOWER(CONCAT('%', :texto, '%')) " +
                "OR LOWER(h.descripcion) LIKE LOWER(CONCAT('%', :texto, '%')) " +
                "OR (h.fuente IS NOT NULL AND LOWER(h.fuente.nombre) LIKE LOWER(CONCAT('%', :texto, '%')))" +
            "))")
    List<Hecho> buscarEnColeccionSegun(
            @Param("coleccionId") Long coleccionId,
            @Param("categoriaNombre") String categoriaNombre,
            @Param("repDesde") LocalDate repDesde,
            @Param("repHasta") LocalDate repHasta,
            @Param("acaDesde") LocalDate acaDesde,
            @Param("acaHasta") LocalDate acaHasta,
            @Param("provincia") String provincia,
            @Param("delta") Float delta,
            @Param("curado") Boolean curado,
            @Param("texto") String texto,
            @Param("fuenteTipo") String fuenteTipo
    );

    @Query("SELECT hxc.hecho FROM HechoXColeccion hxc " +
            "JOIN hxc.hecho h " +
            "WHERE hxc.coleccion.id_coleccion = :coleccionId " +
            "AND (h.estado  IS NULL OR h.estado <> ar.utn.ba.ddsi.apipublica.models.entities.EnumEstadoHecho.BAJA) "+            // Para coordenadas: si ambas son NULL entonces no filtra; si no, compara con un rango (ej. ~1km -> 0.01 grados aprox)
            "AND (:categoriaNombre IS NULL OR LOWER(h.categoria.nombre) = LOWER(:categoriaNombre)) " +
            "AND (:repDesde IS NULL OR h.fechaDeCarga >= :repDesde) " +
            "AND (:repHasta IS NULL OR h.fechaDeCarga <= :repHasta) " +
            "AND (:acaDesde IS NULL OR h.fecha >= :acaDesde) " +
            "AND (:acaHasta IS NULL OR h.fecha <= :acaHasta) " +
            "AND (:provincia IS NULL OR (h.ubicacion IS NOT NULL AND LOWER(h.ubicacion.provincia.nombre) = LOWER(:provincia))) " +
            "AND (:fuenteTipo IS NULL OR h.fuente IS NOT NULL AND STR(h.fuente.tipoFuente) = :fuenteTipo) " +
            "AND (:texto IS NULL OR (" +
            "LOWER(h.titulo) LIKE LOWER(CONCAT('%', :texto, '%')) " +
            "OR LOWER(h.descripcion) LIKE LOWER(CONCAT('%', :texto, '%')) " +
            "OR (h.fuente IS NOT NULL AND LOWER(h.fuente.nombre) LIKE LOWER(CONCAT('%', :texto, '%')))" +
            "))")
    List<Hecho> buscarEnColeccionIrrestricta(
            @Param("coleccionId") Long coleccionId,
            @Param("categoriaNombre") String categoriaNombre,
            @Param("repDesde") LocalDate repDesde,
            @Param("repHasta") LocalDate repHasta,
            @Param("acaDesde") LocalDate acaDesde,
            @Param("acaHasta") LocalDate acaHasta,
            @Param("provincia") String provincia,
            @Param("delta") Float delta,
            @Param("texto") String texto,
            @Param("fuenteTipo") String fuenteTipo
    );

    // Nuevo: búsqueda de colecciones con filtros opcionales
    @Query("SELECT DISTINCT c FROM Coleccion c LEFT JOIN c.fuentes f " +
            "WHERE (:titulo IS NULL OR LOWER(c.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))) " +
            "AND (:descripcion IS NULL OR LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%'))) " +
            "AND (:tipo IS NULL OR c.tipoDeAlgoritmo = :tipo) " +
            "AND (:fuenteIds IS NULL OR f.id_fuente IN :fuenteIds)")
    List<Coleccion> buscarColeccionesSegun(
            @Param("titulo") String titulo,
            @Param("descripcion") String descripcion,
            @Param("tipo") EnumTipoDeAlgoritmo tipo,
            @Param("fuenteIds") List<Long> fuenteIds
    );
}
