package utn.ddsi.agregador.domain.hecho;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ubicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_ubicacion;
    private Float latitud;
    private Float longitud;
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "id_provincia")
    private Provincia provincia;

    public Ubicacion(Float latitud, Float longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }
}
