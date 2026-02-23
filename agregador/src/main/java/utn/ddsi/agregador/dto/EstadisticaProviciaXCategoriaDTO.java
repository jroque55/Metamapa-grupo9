package utn.ddsi.agregador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EstadisticaProviciaXCategoriaDTO {
    private String provincia;
    private Long cantidad;

    public EstadisticaProviciaXCategoriaDTO(String provincia, Long cantidad) {
        this.provincia = provincia;
        this.cantidad = cantidad;
    }
}
