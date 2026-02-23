package utn.ddsi.agregador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EstadisticaColeccionHechosXProvinciaDTO {

    private String provincia;
    private Long cantidad;

    public EstadisticaColeccionHechosXProvinciaDTO(String provincia,Long cantidadDeReportados) {
        this.provincia = provincia;
        this.cantidad = cantidadDeReportados;
    }

}