package ar.utn.ba.ddsi.fuenteproxy.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UbicacionInputDTO {
    private long id_ubicacion;
    private float latitud;
    private float longitud;
    private String provincia;
    private String pais;

}
