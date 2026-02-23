package ar.utn.ba.ddsi.fuenteproxy.models.dtos;


import ar.utn.ba.ddsi.fuenteproxy.models.entities.Fuente;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FuenteDTO {
    private String nombre;
    private String url;
    private String tipoFuente;
    public FuenteDTO() {
    }
    public FuenteDTO(Fuente fuente) {
        this.nombre = fuente.getNombre();
        this.url = fuente.getUrl();
        this.tipoFuente = fuente.getTipoFuente().name();
    }
}
