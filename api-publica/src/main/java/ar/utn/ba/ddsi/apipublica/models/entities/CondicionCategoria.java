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
@DiscriminatorValue("categoria")
public class CondicionCategoria extends InterfaceCondicion {
    @ManyToOne
    private Categoria categoria; // creo que es mas sencillo asi

    @Override
    public boolean cumpleCondicion(Hecho hecho) {
        return hecho.getCategoria().getNombre().equals(this.categoria.getNombre());
    }
}

