package utn.ddsi.agregador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HechoFuenteDinamicaDTO {
    private String titulo;
    private String descripcion;
    private LocalDate fecha;
    private UbicacionDTO ubicacion;
    private String categoria;
    private List<AdjuntoDTO> adjuntos;
    private String tipoDeHecho;
}
