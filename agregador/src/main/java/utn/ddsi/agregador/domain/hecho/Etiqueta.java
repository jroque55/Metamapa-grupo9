package utn.ddsi.agregador.domain.hecho;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Etiqueta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_etiqueta;
    private String nombre;
    public Etiqueta(String nombre) {
        this.nombre = nombre;
    }
}
