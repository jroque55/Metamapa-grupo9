package ar.utn.ba.ddsi.apipublica.models.entities;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("fechaDespues")
public class CondicionFechaDespues extends InterfaceCondicion {
    private LocalDate fechaDespues;

    @Override
    public boolean cumpleCondicion(Hecho hecho) {
        return hecho.getFecha().isAfter(fechaDespues);
    }
}

