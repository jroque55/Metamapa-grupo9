package utn.ddsi.agregador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HechoFuenteEstaticaDTO {
    private String titulo;
    private String descripcion;
    private LocalDate fecha;
    private Float  latitud;
    private Float longitud;
    private FuenteDTO fuente;
    private CategoriaDTO categoria;
}
