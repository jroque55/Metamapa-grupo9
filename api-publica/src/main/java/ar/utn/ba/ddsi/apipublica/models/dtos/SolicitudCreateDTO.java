package ar.utn.ba.ddsi.apipublica.models.dtos;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "SolicitudCreateDTO",
        description = "Datos necesarios para crear una solicitud de eliminación de un hecho"
)
public class SolicitudCreateDTO {
    @Schema(
            description = "ID del hecho sobre el cual se solicita la eliminación",
            example = "12",
            required = true
    )
    private Long idHecho;
    @Schema(
            description = "ID del contribuyente que realiza la solicitud",
            example = "5",
            required = true
    )
    private Long idContribuyente;
    @Schema(
            description = "Motivo por el cual se solicita la eliminación del hecho",
            example = "El hecho contiene información incorrecta",
            required = true
    )
    private String motivo;

    public Long getIdHecho() { return idHecho; }
    public void setIdHecho(Long idHecho) { this.idHecho = idHecho; }

    public Long getIdContribuyente() { return idContribuyente; }
    public void setIdContribuyente(Long idContribuyente) { this.idContribuyente = idContribuyente; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
