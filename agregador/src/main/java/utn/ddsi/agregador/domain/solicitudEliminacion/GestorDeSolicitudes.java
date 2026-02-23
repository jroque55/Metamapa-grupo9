package utn.ddsi.agregador.domain.solicitudEliminacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utn.ddsi.agregador.repository.IRepositorySolicitudes;
import utn.ddsi.agregador.utils.EnumEstadoSol;

@Component
public class GestorDeSolicitudes {
    private final DetectorDeSpam detector;
    private final IRepositorySolicitudes repository;

    @Autowired
    public GestorDeSolicitudes(IRepositorySolicitudes repository,
                               DetectorDeSpam detector) {
        this.repository = repository;
        this.detector = detector;
    }

    public void procesarTodasLasSolicitudes() {
        repository.findAll().forEach(this::procesarSolicitud);
    }
    public void procesarSolicitud(SolicitudEliminacion solicitud) {
        if (detector.esSpam(solicitud.getMotivo())) {
            solicitud.setEstado(EnumEstadoSol.RECHAZADA);
            solicitud.setSpam(true);
            repository.save(solicitud);
        } else {
            solicitud.setEstado(EnumEstadoSol.PENDIENTE);
            repository.save(solicitud);
        }
    }
}