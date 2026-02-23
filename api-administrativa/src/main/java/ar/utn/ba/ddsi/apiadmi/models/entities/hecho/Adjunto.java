package ar.utn.ba.ddsi.apiadmi.models.entities.hecho;
import ar.utn.ba.ddsi.apiadmi.utils.TipoMedia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Adjunto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_adjunto;
    @Enumerated(EnumType.STRING)
    private TipoMedia tipo;
    @Column(length = 100)
    private String url;
}