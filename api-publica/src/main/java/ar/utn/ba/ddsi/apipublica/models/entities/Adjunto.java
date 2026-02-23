package ar.utn.ba.ddsi.apipublica.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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

    public Adjunto(TipoMedia tipo, String url) {
        this.tipo = tipo;
        this.url = url;
    }
}
