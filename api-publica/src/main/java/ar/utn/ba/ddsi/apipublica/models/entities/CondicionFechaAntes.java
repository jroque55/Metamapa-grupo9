package ar.utn.ba.ddsi.apipublica.models.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/* Se separa en dos clases para distinguir las condiciones*/
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("fechaAntes")
public class CondicionFechaAntes extends InterfaceCondicion {
    private LocalDate fechaAntes;

    @Override
    public boolean cumpleCondicion(Hecho hecho) {
        return hecho.getFecha().isBefore(fechaAntes);
    }
}

