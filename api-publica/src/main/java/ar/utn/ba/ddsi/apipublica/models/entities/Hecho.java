package ar.utn.ba.ddsi.apipublica.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "hecho")
public class Hecho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_hecho;
    //getters y setters
    @Column(name = "titulo")
    private String titulo;
    @Column(name="descripcion",length = 1000)
    private String descripcion;
    @ManyToOne
    private Categoria categoria;
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;
    @Column(name = "fechaCarga", nullable = false)
    private LocalDateTime fechaDeCarga;
    @ManyToOne(cascade = CascadeType.ALL)
    private Fuente fuente;
    @ManyToOne
    private Ubicacion ubicacion;
    @ManyToOne
    private Etiqueta etiqueta;
    @Enumerated(EnumType.STRING)
    private EnumTipoHecho tipoHecho;
    @OneToMany
    private List<Adjunto> adjuntos = new ArrayList<>();
    @Column(name="estadoHecho")
    private EnumEstadoHecho estado;

    public Hecho(String titulo, String descripcion, Categoria categoria,Ubicacion ubicacion, LocalDate fecha, Fuente fuente, EnumTipoHecho tipoHecho,Etiqueta etiqueta,List<Adjunto> adjuntos) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.fechaDeCarga = LocalDateTime.now();
        this.etiqueta = null;
        this.fuente = fuente;
        this.tipoHecho = tipoHecho;
        this.etiqueta = etiqueta;
        this.adjuntos = adjuntos;

    }

}
