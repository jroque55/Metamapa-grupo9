package ar.utn.ba.ddsi.apiadmi.models.entities.coleccion;

import ar.utn.ba.ddsi.apiadmi.models.entities.fuente.Fuente;
import ar.utn.ba.ddsi.apiadmi.models.entities.condiciones.InterfaceCondicion;
import ar.utn.ba.ddsi.apiadmi.utils.EnumTipoDeAlgoritmo;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="coleccion")
public class Coleccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_coleccion; //cambiar a todos y al DC
    @Column(nullable = false)
    private String titulo;
    @Column(length = 1000)
    private String descripcion;
    @ManyToMany
    @JoinTable(
            name = "fuente_x_coleccion",
            joinColumns = @JoinColumn(name = "id_coleccion"),
            inverseJoinColumns = @JoinColumn(name = "id_fuente")
    )
    private List<Fuente> fuentes;
    @OneToMany(mappedBy = "coleccion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HechoXColeccion> hechos;
    @ManyToMany (cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "condicion_x_coleccion",
            joinColumns = @JoinColumn(name = "id_coleccion"),
            inverseJoinColumns = @JoinColumn(name = "id_condicion")
    )
    private List<InterfaceCondicion> condicionDePertenencia;
    @Enumerated(EnumType.STRING)
    private EnumTipoDeAlgoritmo tipoDeAlgoritmo;

    public Coleccion(String titulo, String descripcion, List<Fuente> fuentes) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fuentes = fuentes;
        this.hechos = new ArrayList<>();
        this.condicionDePertenencia = new ArrayList<>();
        this.tipoDeAlgoritmo = EnumTipoDeAlgoritmo.DEFAULT;

    }
    public Coleccion(String titulo, String descripcion, List<Fuente> fuentes, EnumTipoDeAlgoritmo tipoDeAlgoritmo) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fuentes = fuentes;
        this.hechos = new ArrayList<>();
        this.condicionDePertenencia = new ArrayList<>();
        this.tipoDeAlgoritmo =tipoDeAlgoritmo;

    }

    public Coleccion() {
    }

    public void addFuente(Fuente fuente) {
        this.fuentes.add(fuente);
    }
}