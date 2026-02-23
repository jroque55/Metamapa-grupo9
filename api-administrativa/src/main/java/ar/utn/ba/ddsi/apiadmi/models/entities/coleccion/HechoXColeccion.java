package ar.utn.ba.ddsi.apiadmi.models.entities.coleccion;


import ar.utn.ba.ddsi.apiadmi.models.entities.hecho.Hecho;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;


@Data
@NoArgsConstructor
@Entity
@Table(name = "hecho_x_coleccion")
public class HechoXColeccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_hecho_x_coleccion;

    @ManyToOne
    @JoinColumn(name = "id_hecho")
    private Hecho hecho;

    @ManyToOne
    @JoinColumn(name = "id_coleccion")
    private Coleccion coleccion;

    @Column(name = "consensuado", nullable = false)
    @ColumnDefault("0")
    private Boolean consensuado;

    public HechoXColeccion(Hecho hecho, Coleccion coleccion, Boolean consensuado) {
        this.hecho = hecho;
        this.coleccion = coleccion;
        this.consensuado = consensuado;
    }
}