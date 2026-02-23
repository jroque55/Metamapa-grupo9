package utn.ddsi.agregador.domain.hecho;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Provincia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_provincia;
    private String nombre; //nombre de la provincia o estado federal
    private String pais;
}
