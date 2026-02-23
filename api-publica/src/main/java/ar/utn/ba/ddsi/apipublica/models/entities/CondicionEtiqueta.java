package ar.utn.ba.ddsi.apipublica.models.entities;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
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
@DiscriminatorValue("etiqueta")
public class CondicionEtiqueta extends InterfaceCondicion {
    @ManyToOne
    private Etiqueta etiqueta;

    @Override
    public boolean cumpleCondicion(Hecho hecho) {
        return hecho.getEtiqueta().getNombre().equals(this.etiqueta.getNombre());
    }
}
