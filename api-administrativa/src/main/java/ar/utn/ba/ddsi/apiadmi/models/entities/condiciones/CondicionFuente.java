package ar.utn.ba.ddsi.apiadmi.models.entities.condiciones;

import ar.utn.ba.ddsi.apiadmi.models.entities.fuente.Fuente;
import ar.utn.ba.ddsi.apiadmi.models.entities.hecho.Hecho;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("fuente")
public class CondicionFuente extends InterfaceCondicion {

    @ManyToOne
    private Fuente fuente;


    @Override
    public String tipo(){
        return "fuente";
    }
    @Override
    public String valor() {
        return fuente.getNombre();
    }
}