package utn.ddsi.agregador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EstadisticaCantidadHoraCateDTO {
    private Integer hora;
    private Long cantidad;

    public EstadisticaCantidadHoraCateDTO(Long cantidad, Integer hora) {
        this.cantidad = cantidad;
        this.hora = hora;
    }
}
