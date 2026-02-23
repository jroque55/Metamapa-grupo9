package ar.utn.ba.ddsi.apiadmi.models.entities.condiciones;


import ar.utn.ba.ddsi.apiadmi.models.entities.hecho.Hecho;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDate;

/* Se separa en dos clases para distinguir las condiciones*/
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("fechaAntes")
public class CondicionFechaANTES extends InterfaceCondicion {
    private LocalDate fechaAntes;

    @Override
    public String tipo(){
        return "fechaAntes";
    }
    @Override
    public String valor() {
        return fechaAntes.toString();
    }
}