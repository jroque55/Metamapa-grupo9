package utn.ddsi.agregador.domain.condicion;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Data;
import utn.ddsi.agregador.domain.hecho.Hecho;

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

