package utn.ddsi.agregador.domain.fuentes;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import utn.ddsi.agregador.utils.EnumTipoFuente;

@Getter
@Setter
@Entity
public class Fuente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_fuente;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String url;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumTipoFuente tipoFuente;

    public Fuente(String nombre, String url, EnumTipoFuente tipoFuente) {
        this.nombre = nombre;
        this.url = url;
        this.tipoFuente = tipoFuente;
    }
    public Fuente() {
    }
}