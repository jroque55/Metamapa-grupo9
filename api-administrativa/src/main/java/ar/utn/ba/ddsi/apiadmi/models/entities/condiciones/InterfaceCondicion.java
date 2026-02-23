package ar.utn.ba.ddsi.apiadmi.models.entities.condiciones;

import ar.utn.ba.ddsi.apiadmi.models.entities.hecho.Hecho;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "condicion")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)// DISCUTIR POR WHASATP
@DiscriminatorColumn(name = "Condicion", discriminatorType = DiscriminatorType.STRING)
public abstract class InterfaceCondicion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_condicion;



    public abstract String tipo();
    public abstract String valor();
}