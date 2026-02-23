package ar.utn.ba.ddsi.apiadmi.servicies;

import ar.utn.ba.ddsi.apiadmi.models.dtos.input.SolicitudInput;
import ar.utn.ba.ddsi.apiadmi.utils.EnumEstadoSol;
import ar.utn.ba.ddsi.apiadmi.models.entities.hecho.Hecho;
import ar.utn.ba.ddsi.apiadmi.models.entities.admin.SolicitudEliminacion;
import ar.utn.ba.ddsi.apiadmi.models.repository.ISolicitudRepository;
import ar.utn.ba.ddsi.apiadmi.servicies.interfaces.IHechoService;
import ar.utn.ba.ddsi.apiadmi.servicies.interfaces.ISolicitudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class SolicitudesService implements ISolicitudService {

    private final ISolicitudRepository solicitudRepo;
    private final IHechoService hechoService;

    @Autowired
    public SolicitudesService(ISolicitudRepository solicitudRepo, IHechoService hechoService) {
        this.solicitudRepo = solicitudRepo;
        this.hechoService = hechoService;
    }

    @Override
    public SolicitudEliminacion actualizarEstado(Long id, SolicitudInput solo) {
        SolicitudEliminacion solicitud = this.solicitudRepo.findById(id)
                .orElseThrow(() ->{
                    log.error("Solicitud con ID {} no encontrada", id);
                     return new RuntimeException("Solicitud no encontrada");}
                );

        String estadoInput = solo.getEstado().toUpperCase();
        Hecho hecho = solicitud.getHecho();

        try {
            EnumEstadoSol nuevoEstado = EnumEstadoSol.valueOf(estadoInput);
            solicitud.setEstado(nuevoEstado);
            this.hechoService.actualizarElEstadoDelHecho(hecho, nuevoEstado);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado inválido: " + solo.getEstado());
        }

        solicitudRepo.save(solicitud);
        return solicitud;
    }
    @Override
    public void eliminarSolicitud(Long id) {
        SolicitudEliminacion solicitud = solicitudRepo.findById(id)
                .orElseThrow(() ->{
                    log.warn("No se encontró la solicitud con ID: {}", id);
                     return new RuntimeException("Solicitud no encontrada");
                });
        if (solicitud.getEstado() != EnumEstadoSol.RECHAZADA) {

            log.warn("Intento de eliminación de solicitud con ID {} que no está rechazada", id);
            throw new IllegalStateException("Solo se pueden eliminar solicitudes rechazadas");
        }
        solicitudRepo.delete(solicitud);

        log.info("Solicitud ID {} eliminada correctamente", id);
    }
}
