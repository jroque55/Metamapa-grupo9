package utn.ddsi.agregador.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EstadisticaCategoriaDTO {

   // private String id_categoria
    private String categoria;
    private Long cantidad;

    public EstadisticaCategoriaDTO(String categoria,Long cantidad){
        this.categoria= categoria;
        this.cantidad = cantidad;
    }
}
