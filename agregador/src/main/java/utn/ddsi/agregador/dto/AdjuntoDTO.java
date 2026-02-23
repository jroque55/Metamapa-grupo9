package utn.ddsi.agregador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdjuntoDTO {
    private long id;
    private String url;
    private String tipo;
}
