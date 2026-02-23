package ar.utn.ba.ddsi.apipublica.models.entities;


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

    public boolean cumpleCondicion(Hecho hecho) {
        return false;
    }
}

