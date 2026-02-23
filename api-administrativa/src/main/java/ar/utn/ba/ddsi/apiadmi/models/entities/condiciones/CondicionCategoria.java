package ar.utn.ba.ddsi.apiadmi.models.entities.condiciones;

import ar.utn.ba.ddsi.apiadmi.models.entities.hecho.Categoria;
import ar.utn.ba.ddsi.apiadmi.models.entities.hecho.Hecho;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("categoria")
public class CondicionCategoria extends InterfaceCondicion {

    @ManyToOne
    private Categoria categoria; // creo que es mas sencillo asi

    @Override
    public String tipo(){
        return "categoria";
    }

    @Override
    public String valor() {
        return this.categoria.getNombre();
    }
}