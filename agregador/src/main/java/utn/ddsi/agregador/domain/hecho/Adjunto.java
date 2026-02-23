package utn.ddsi.agregador.domain.hecho;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utn.ddsi.agregador.utils.TipoMedia;

@Getter
@Setter
@Entity
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
