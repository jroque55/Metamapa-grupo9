package utn.ddsi.agregador.domain.hecho;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import utn.ddsi.agregador.domain.fuentes.Fuente;
import utn.ddsi.agregador.utils.EnumEstadoHecho;
import utn.ddsi.agregador.utils.EnumTipoHecho;

import java.time.LocalDate;
import java.util.Objects;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "hecho")
//@EqualsAndHashCode(of = {"titulo", "descripcion", "categoria", "fecha", "ubicacion", "etiqueta"})
public class Hecho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_hecho;
    //getters y setters
    @Column(name = "titulo")
    private String titulo;
    @Column(name="descripcion",length = 1000)
    private String descripcion;
    @ManyToOne(cascade = {CascadeType.MERGE})
    private Categoria categoria;
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;
    @Column(name = "fechaCarga", nullable = false)
    // PASA DE LOCALDATE A LOCALDATETIME PORQUE ESTADISTICA NECESITA LA HORA, Y
    // LOCALDATE SOLO NOS INDICA EL AÑO, MES Y DIA
    private LocalDateTime fechaDeCarga;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Fuente fuente;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Ubicacion ubicacion;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Etiqueta etiqueta;
    @Enumerated(EnumType.STRING)
    private EnumTipoHecho tipoHecho;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Adjunto> adjuntos;
    @Column(name="estadoHecho")
    private EnumEstadoHecho estado;

    public Hecho(String titulo, String descripcion, Categoria categoria,Ubicacion ubicacion, LocalDate fecha, Fuente fuente) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.fechaDeCarga = LocalDateTime.now();
        this.etiqueta = null;
        this.fuente = fuente;
    }
/*
    @Override
    public String toString() {
        return "Hecho{" +
                "id_hecho=" + id_hecho +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", categoria=" + categoria +
                ", fecha=" + fecha +
                ", fechaDeCarga=" + fechaDeCarga +
                ", fuente=" + fuente +
                ", ubicacion=" + ubicacion +
                ", etiqueta=" + etiqueta +
                ", tipoHecho=" + tipoHecho +
                ", adjuntos=" + adjuntos +
                ", estado=" + estado +
                '}';
    }*/
}
