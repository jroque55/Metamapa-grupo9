package utn.ddsi.agregador.domain.coleccion;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import utn.ddsi.agregador.domain.hecho.Hecho;

@Data
@NoArgsConstructor
@Entity
@Getter
@Setter
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
    private Boolean consensuado=false;

    public HechoXColeccion(Hecho hecho, Coleccion coleccion, Boolean consensuado) {
        this.hecho = hecho;
        this.coleccion = coleccion;
        this.consensuado = consensuado;
    }
}

