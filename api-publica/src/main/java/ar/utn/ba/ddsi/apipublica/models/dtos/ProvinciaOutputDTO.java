package ar.utn.ba.ddsi.apipublica.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Representación de una provincia")
public class ProvinciaOutputDTO {
    @Schema(
            description = "Nombre de la provincia",
            example = "Buenos Aires"
    )
    private String nombre;
    @Schema(
            description = "País al que pertenece la provincia",
            example = "Argentina"
    )
    private String pais;

    public ProvinciaOutputDTO(String nombre,String pais) {
        this.nombre = nombre;
        this.pais = pais;
    }
}
