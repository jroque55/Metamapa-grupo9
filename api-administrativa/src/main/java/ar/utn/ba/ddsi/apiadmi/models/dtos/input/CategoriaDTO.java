package ar.utn.ba.ddsi.apiadmi.models.dtos.input;

import lombok.Data;

@Data
public class CategoriaDTO {
    private String nombre;

    public CategoriaDTO(String nombre) {
        this.nombre = nombre;
    }
}
