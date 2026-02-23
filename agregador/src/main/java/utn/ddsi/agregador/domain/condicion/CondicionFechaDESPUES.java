package utn.ddsi.agregador.domain.condicion;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utn.ddsi.agregador.domain.hecho.Hecho;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("fechaDespues")
public class CondicionFechaDESPUES extends InterfaceCondicion {
    private LocalDate fechaDespues;

    @Override
    public boolean cumpleCondicion(Hecho hecho) {
        return hecho.getFecha().isAfter(fechaDespues);
    }
}
