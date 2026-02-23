package ar.utn.ba.ddsi.apiadmi.models.entities.admin;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Contribuyente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_contribuyente;

    private String nombre;
    private String apellido;
    private int edad;
    private Boolean anonimo;

    public Contribuyente() {
    }

    public Contribuyente(Long id, String nombre, String apellido, int edad) {
        if (edad < 0) {
            throw new IllegalArgumentException("La edad no puede ser negativa.");
        }
        this.id_contribuyente = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.anonimo = false;
    }
}
