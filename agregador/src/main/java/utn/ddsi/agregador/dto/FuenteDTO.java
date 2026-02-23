package utn.ddsi.agregador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utn.ddsi.agregador.utils.EnumTipoFuente;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuenteDTO {
    private String nombre;
    private String ruta;
    private EnumTipoFuente tipoFuente;
}
