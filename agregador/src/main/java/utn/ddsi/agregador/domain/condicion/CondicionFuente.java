package utn.ddsi.agregador.domain.condicion;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utn.ddsi.agregador.domain.fuentes.Fuente;
import utn.ddsi.agregador.domain.hecho.Hecho;

@Entity
@Getter
@Setter

@DiscriminatorValue("fuente")
public class CondicionFuente extends InterfaceCondicion {

    @ManyToOne
    private Fuente fuente;

    public CondicionFuente(Fuente fuente) {
        this.fuente = fuente;
    }

    @Override
    public boolean cumpleCondicion(Hecho hecho) {
        return fuente.equals(hecho.getFuente());
    }
}
