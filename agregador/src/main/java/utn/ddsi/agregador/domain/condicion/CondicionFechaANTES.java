package utn.ddsi.agregador.domain.condicion;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utn.ddsi.agregador.domain.hecho.Hecho;

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
    public boolean cumpleCondicion(Hecho hecho) {
        return hecho.getFecha().isBefore(fechaAntes);
    }
}
