package utn.ddsi.agregador.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.ddsi.agregador.dto.EstadisticaSolicitudesDTO;
import utn.ddsi.agregador.repository.IRepositorySolicitudes;

import java.time.LocalDate;

@Slf4j
@Service
public class ServiceSolicitudes {
    @Autowired
    private final IRepositorySolicitudes repoSolicitudes;

    public ServiceSolicitudes(IRepositorySolicitudes repoSolicitudes) {
        this.repoSolicitudes = repoSolicitudes;
    }

    public EstadisticaSolicitudesDTO cantidadSolicitudesSpam() {
        log.info("ServiceSolicitudes: Iniciando busqueda de estadísticas de SPAM.");

        EstadisticaSolicitudesDTO estadistica = this.repoSolicitudes.obtenerEstadisticas();

        if (estadistica == null) {
            log.warn("ServiceSolicitudes: La consulta de estadísticas de SPAM devolvió null.");
        } else {
            log.debug("ServiceSolicitudes: Estadísticas recuperadas exitosamente.");
            // Si el DTO tiene un toString, esto es muy útil para debug
            log.info("ServiceSolicitudes: Datos de SPAM obtenidos.");
        }

        return estadistica;
    }
    

}
