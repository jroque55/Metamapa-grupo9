package ar.utn.ba.ddsi.apiadmi.models.entities.hecho;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


@Data
@Entity
public class Provincia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_provincia;
    private String nombre; //nombre de la provincia o estado federal
    private String pais;
}