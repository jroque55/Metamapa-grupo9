package ar.utn.ba.ddsi.apiadmi.models.dtos.input;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SolicitudInput {
    @NotBlank(message = "El estado no puede estar vacío")
    private String estado;
}
