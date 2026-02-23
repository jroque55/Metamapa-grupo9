package ar.utn.ba.ddsi.apipublica.models.dtos;

import ar.utn.ba.ddsi.apipublica.models.entities.Ubicacion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UbicacionOutputDTO {
    private long id_ubicacion;
    private float latitud;
    private float longitud;
    private String provincia;
    private String pais;

    public UbicacionOutputDTO(long id_ubicacion, float latitud, float longitud, String nombre, String pais) {
        this.id_ubicacion = id_ubicacion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.provincia = nombre;
        this.pais = pais;
    }
    public UbicacionOutputDTO(Ubicacion ubicacion){
        this.id_ubicacion = ubicacion.getIdUbicacion();
        this.latitud = ubicacion.getLatitud();
        this.longitud = ubicacion.getLongitud();
        this.provincia = ubicacion.getProvincia().getNombre();
        this.pais = ubicacion.getProvincia().getPais();
    }

}
