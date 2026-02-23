package utn.ddsi.agregador.domain.hecho;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_categoria;
    private String nombre;
    public Categoria(String nombre) {
        this.nombre = nombre;
    }
}
