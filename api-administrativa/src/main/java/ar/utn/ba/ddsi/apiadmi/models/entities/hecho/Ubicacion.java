package ar.utn.ba.ddsi.apiadmi.models.entities.hecho;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Ubicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_ubicacion;
    private float latitud;
    private float longitud;
    @ManyToOne
    @JoinColumn(name = "id_provincia")
    private Provincia provincia;

    public Ubicacion(float latitud, float longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }
}
