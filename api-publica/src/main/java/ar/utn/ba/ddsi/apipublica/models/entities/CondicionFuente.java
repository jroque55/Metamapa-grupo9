package ar.utn.ba.ddsi.apipublica.models.entities;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("fuente")
public class CondicionFuente extends InterfaceCondicion {

    @ManyToOne
    private Fuente fuente;

    @Override
    public boolean cumpleCondicion(Hecho hecho) {
        return fuente.equals(hecho.getFuente());
    }
}
