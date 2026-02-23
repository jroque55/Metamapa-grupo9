package ar.utn.ba.ddsi.apiadmi.servicies.interfaces;

import ar.utn.ba.ddsi.apiadmi.models.dtos.input.SolicitudInput;
import ar.utn.ba.ddsi.apiadmi.models.entities.admin.SolicitudEliminacion;

public interface ISolicitudService {
    SolicitudEliminacion actualizarEstado(Long id, SolicitudInput solo);
    void eliminarSolicitud(Long id);
}
