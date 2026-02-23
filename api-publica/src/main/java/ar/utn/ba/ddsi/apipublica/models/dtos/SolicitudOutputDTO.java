package ar.utn.ba.ddsi.apipublica.models.dtos;

import ar.utn.ba.ddsi.apipublica.models.entities.SolicitudEliminacion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(
        name = "SolicitudOutputDTO",
        description = "Información completa de una solicitud de eliminación"
)
public class SolicitudOutputDTO {
    @Schema(
            description = "ID único de la solicitud",
            example = "100"
    )
    private Long id_solicitud;
    @Schema(
            description = "Hecho asociado a la solicitud"
    )
    private HechoOutputDTO hecho;
    @Schema(
            description = "Motivo declarado por el solicitante",
            example = "Información errónea"
    )
    private String motivo;
    @Schema(
            description = "Estado actual de la solicitud",
            example = "PENDIENTE"
    )
    private String estadoSolicitud;
    @Schema(
            description = "ID del contribuyente solicitante",
            example = "5"
    )
    private Long id_contribuyente;
    @Schema(
            description = "Fecha en la que se creó la solicitud",
            example = "2024-06-12"
    )
    private LocalDate fechaSolicitud;
    @Schema(
            description = "Indica si la solicitud fue marcada como spam",
            example = "false"
    )
    private boolean spam;

    public SolicitudOutputDTO(Long id_solicitud, HechoOutputDTO hecho, String estadoSolicitud, String motivo, Long id_contribuyente, boolean spam) {
        this.id_solicitud = id_solicitud;
        this.hecho = hecho;
        this.estadoSolicitud = estadoSolicitud;
        this.motivo = motivo;
        this.id_contribuyente = id_contribuyente;
        this.spam = spam;
    }
    // Constructor que recibe SolicitudEliminacion
    public SolicitudOutputDTO(SolicitudEliminacion solicitud) {
        this.id_solicitud = solicitud.getId_solicitud();
        // Asumiendo que HechoOutputDTO tiene un constructor que acepta un objeto Hecho
        this.hecho = new HechoOutputDTO(solicitud.getHecho());
        this.motivo = solicitud.getMotivo();
        // Convierte el EnumEstadoSol a su representación en String
        this.estadoSolicitud = solicitud.getEstado().name();
        // Asumiendo que 'solicitante' de SolicitudEliminacion no es nulo y tiene un método getId()
        this.id_contribuyente = solicitud.getId_solicitante() != null ? solicitud.getId_solicitante() : null;
        this.spam = solicitud.isSpam();
        this.fechaSolicitud = solicitud.getFecha();
    }
}
