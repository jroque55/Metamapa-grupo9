package utn.ddsi.agregador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utn.ddsi.agregador.domain.solicitudEliminacion.SolicitudEliminacion;
import utn.ddsi.agregador.dto.EstadisticaSolicitudesDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface IRepositorySolicitudes extends JpaRepository<SolicitudEliminacion,Long> {

    @Query("""
    SELECT COUNT(a)
    FROM SolicitudEliminacion a
    WHERE a.spam = true
      AND a.fecha >= :fechaLimite
    """)
    Long contarSolicitudesSpamDesde();

    @Query("""
    SELECT new utn.ddsi.agregador.dto.EstadisticaSolicitudesDTO(
        COUNT(a),
        SUM(CASE WHEN a.spam = true THEN 1 ELSE 0 END)
    )
    FROM SolicitudEliminacion a
""")
    EstadisticaSolicitudesDTO obtenerEstadisticas();
}
