package ar.utn.ba.ddsi.apiadmi.models.dtos;

import ar.utn.ba.ddsi.apiadmi.models.entities.fuente.Fuente;
import lombok.Data;

import java.util.List;

@Data
public class ColeccionDto {
    private Long id_coleccion;
    private String titulo;
    private String descripcion;
    private List<CondicionDTO> criterios ;
    private String algoritmoDeConsenso;
    private List<Fuente> fuentes;

    /*
    public void addFuentes(List<String> fuentes) {
        this.fuentes.addAll(fuentes);
    }

     */


}
