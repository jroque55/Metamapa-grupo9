package ar.utn.ba.ddsi.apipublica.models.dtos;

import lombok.Data;

@Data
public class CategoriaOutputDTO {
    private String nombre;

    public CategoriaOutputDTO( String nombre) {
        this.nombre = nombre;
    }
}
