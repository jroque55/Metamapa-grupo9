package ar.utn.ba.ddsi.apiadmi.models.entities.hecho;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Etiqueta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_etiqueta;
    private String nombre;

    public Etiqueta(String nombre) {
        this.nombre = nombre;
    }

    public Etiqueta() {

    }
}
