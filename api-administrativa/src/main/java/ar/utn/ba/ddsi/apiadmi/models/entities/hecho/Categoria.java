package ar.utn.ba.ddsi.apiadmi.models.entities.hecho;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity

public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_categoria;
    private String nombre;
    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    public Categoria(){}
}