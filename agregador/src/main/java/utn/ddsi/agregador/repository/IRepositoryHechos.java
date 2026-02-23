package utn.ddsi.agregador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utn.ddsi.agregador.domain.hecho.Hecho;
import utn.ddsi.agregador.dto.EstadisticaCantidadHoraCateDTO;
import utn.ddsi.agregador.dto.EstadisticaCategoriaDTO;
import utn.ddsi.agregador.dto.EstadisticaProviciaXCategoriaDTO;

import java.util.List;

@Repository
public interface IRepositoryHechos extends JpaRepository<Hecho, Long> {

    @Query("SELECT COUNT(*) FROM Hecho h " +
            "WHERE h.categoria = :id_categoria"
    )
    Long contarHechosDeCategoria(@Param("id_categoria") Long idCategoria);

    @Query("SELECT new utn.ddsi.agregador.dto.EstadisticaCategoriaDTO(c.nombre, COUNT(*)) FROM Hecho h " +
        "JOIN h.categoria c " +
        "group by c.nombre"
    )
    List<EstadisticaCategoriaDTO> contarHechosDeCategorias();

    @Query("SELECT new utn.ddsi.agregador.dto.EstadisticaProviciaXCategoriaDTO(pro.nombre, " +
            "COUNT(h)) " +
            "FROM Hecho h " +
            "JOIN h.ubicacion ubi " +
            "JOIN ubi.provincia pro " +
            " WHERE h.categoria.id_categoria = :categoria " +
            " group by pro.nombre "
    )
    List<EstadisticaProviciaXCategoriaDTO> obtenerCantidadDeHechosXProvinciaXCategoria(@Param("categoria") Long categoria);

    @Query("SELECT new utn.ddsi.agregador.dto.EstadisticaCantidadHoraCateDTO(" +
            " HOUR(h.fechaDeCarga), " +
            " COUNT(h)) " +
            " FROM Hecho h " +
            " WHERE h.categoria.id_categoria = :categoria " +
            " GROUP BY HOUR(h.fechaDeCarga) " +
            " ORDER BY COUNT(h) DESC")
    List<EstadisticaCantidadHoraCateDTO> obtenerCantidadDeHechosXDiaXCategoria(@Param("categoria") Long categoria);

    Hecho findFirstByTitulo(String titulo);
}