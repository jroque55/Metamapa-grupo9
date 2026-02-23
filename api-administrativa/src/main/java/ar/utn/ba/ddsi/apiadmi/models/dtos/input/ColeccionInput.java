package ar.utn.ba.ddsi.apiadmi.models.dtos.input;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColeccionInput {


    @NotBlank
    private String titulo;
    private String descripcion;
    @NotEmpty
    private List<String> fuentes;
    @NotNull
    private List<CondicionInput> criterios;

    @NotBlank
    private String algoritmoConcenso;


}


