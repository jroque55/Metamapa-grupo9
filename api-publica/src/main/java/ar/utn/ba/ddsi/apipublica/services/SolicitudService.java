package ar.utn.ba.ddsi.apipublica.services;

import ar.utn.ba.ddsi.apipublica.exception.RecursoNoEncontradoException;
import ar.utn.ba.ddsi.apipublica.models.dtos.SolicitudCreateDTO;
import ar.utn.ba.ddsi.apipublica.models.dtos.SolicitudOutputDTO;
import ar.utn.ba.ddsi.apipublica.models.entities.Hecho;
import ar.utn.ba.ddsi.apipublica.models.entities.SolicitudEliminacion;
import ar.utn.ba.ddsi.apipublica.models.repository.HechoRepository;
import ar.utn.ba.ddsi.apipublica.models.repository.SolicitudRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class SolicitudService implements ISolicitudService{

    private final SolicitudRepository solicitudRepository;
    private final HechoRepository hechoRepository;

    public SolicitudService(SolicitudRepository solicitudRepository, HechoRepository hechoRepository) {
        this.solicitudRepository = solicitudRepository;
        this.hechoRepository = hechoRepository;
    }

    @Override
    public SolicitudEliminacion crearSolicitud(SolicitudCreateDTO dto) throws IllegalArgumentException{

        log.info("Iniciando creación de solicitud");
        if (dto == null){
            log.warn("Intento de crear solicitud con datos nulos");
            throw new IllegalArgumentException("Datos de la solicitud vacios");
        }
        if (dto.getMotivo() == null || dto.getMotivo().trim().isEmpty()){
            log.warn("Intento de crear solicitud sin motivo");
            throw new IllegalArgumentException("Motivo requerido");
        }

        Hecho hecho = hechoRepository.findById(dto.getIdHecho()).orElse(null);
        if (hecho == null){
            log.warn("Intento de crear solicitud para hecho con ID {} que no existe", dto.getIdHecho());
            throw new IllegalArgumentException("Hecho no encontrado");
        }
        //DEbería chequear que existe el id? Por ahora pensamos que si manda solicitud es porque etsá bien.

        //Contribuyente solicitante = contribuyenteRepository.findById(dto.getIdContribuyente()).orElse(null);
        //if (solicitante == null) throw new IllegalArgumentException("Contribuyente no encontrado");

        SolicitudEliminacion solicitud = new SolicitudEliminacion(dto.getIdContribuyente(), hecho, LocalDate.now(), dto.getMotivo());
        return solicitudRepository.save(solicitud);
    }

    @Override
    public List<SolicitudOutputDTO> listarSolicitudes() {
        log.info("Obteniendo todas las solicitudes de eliminación");
        List<SolicitudEliminacion> solicitudes = solicitudRepository.findAll();
        List<SolicitudOutputDTO> solicitudesDTO = new ArrayList<>();
        solicitudes.forEach(solicitud -> {
            solicitudesDTO.add(new SolicitudOutputDTO(solicitud));
        });

        log.debug("Total de solicitudes obtenidas: {}", solicitudesDTO.size());
        return solicitudesDTO;
    }

    @Override
    public SolicitudOutputDTO obtenerSolicitudPorId(Long id) {
        log.info("Obteniendo solicitud de eliminación con ID: {}", id);
        SolicitudEliminacion solicitud = this.solicitudRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no encontrada"));
        return new SolicitudOutputDTO(solicitud);

    }
}
