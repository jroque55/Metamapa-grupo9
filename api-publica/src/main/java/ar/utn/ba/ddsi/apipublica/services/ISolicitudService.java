package ar.utn.ba.ddsi.apipublica.services;

import ar.utn.ba.ddsi.apipublica.models.dtos.SolicitudCreateDTO;
import ar.utn.ba.ddsi.apipublica.models.dtos.SolicitudOutputDTO;
import ar.utn.ba.ddsi.apipublica.models.entities.SolicitudEliminacion;

import java.util.List;

public interface ISolicitudService {
    SolicitudEliminacion crearSolicitud(SolicitudCreateDTO dto) throws IllegalArgumentException;
    List<SolicitudOutputDTO> listarSolicitudes();
    SolicitudOutputDTO obtenerSolicitudPorId(Long id);
}
