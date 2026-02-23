package ar.utn.ba.ddsi.apiadmi.models.entities.condiciones;

import ar.utn.ba.ddsi.apiadmi.models.entities.hecho.Etiqueta;
import ar.utn.ba.ddsi.apiadmi.models.entities.hecho.Hecho;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

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
    public String tipo(){
        return "etiqueta";
    }

    @Override
    public String valor() {
        return this.etiqueta.getNombre();
    }
}