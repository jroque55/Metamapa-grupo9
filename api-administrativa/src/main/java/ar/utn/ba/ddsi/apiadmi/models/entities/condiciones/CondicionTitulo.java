package ar.utn.ba.ddsi.apiadmi.models.entities.condiciones;

import ar.utn.ba.ddsi.apiadmi.models.entities.hecho.Hecho;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("titulo")
public class CondicionTitulo extends InterfaceCondicion {
    private String titulo;

    @Override
    public String tipo(){
        return "titulo";
    }

    @Override
    public String valor() {
        return titulo;
    }
}