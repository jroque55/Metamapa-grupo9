package ar.utn.ba.ddsi.apiadmi.models.entities.condiciones;

import ar.utn.ba.ddsi.apiadmi.models.entities.hecho.Hecho;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDate;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("fechaDespues")
public class CondicionFechaDESPUES extends InterfaceCondicion {
    private LocalDate fechaDespues;


    @Override
    public String tipo() {
        return "fechaDespues";
    }


    @Override
    public String valor() {
        return fechaDespues.toString();
    }
}

