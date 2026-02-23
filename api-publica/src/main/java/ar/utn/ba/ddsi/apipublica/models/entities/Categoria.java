package ar.utn.ba.ddsi.apipublica.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_categoria;

    private String nombre;

    public  Categoria() {}

    public Categoria(String nombre) {
        this.nombre = nombre;
    }


}
