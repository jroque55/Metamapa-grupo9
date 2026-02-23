package ar.utn.ba.ddsi.apipublica.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Getter
@Setter
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
    @ManyToMany
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

    public Coleccion() {

    }
    public List<Fuente> obtenerFuentes() {
        List<Fuente> fuentes = this.hechos.stream().map(hxc -> hxc.getHecho().getFuente()).distinct().collect(Collectors.toList());
        return fuentes;
    }

    public void agregarHechos(List<Hecho> nuevosHechos) {
        nuevosHechos.forEach(hecho -> {
            HechoXColeccion hxc = new HechoXColeccion(hecho, this, false);
            hechos.add(hxc);
        });
    }

    public void cambiarCriterioDePertenencia(List<InterfaceCondicion> criterio) {
        condicionDePertenencia = criterio;
    }

    // Método para obtener solo los hechos consensuados (para navegación curada)
    public List<Hecho> obtenerSoloHechosConsensuados() {
        return hechos.stream()
                .filter(HechoXColeccion::getConsensuado)
                .map(HechoXColeccion::getHecho)
                .collect(Collectors.toList());
    }

    // Método para obtener todos los hechos (para navegación no curada)
    public List<Hecho> obtenerTodosLosHechos() {
        return hechos.stream()
                .map(HechoXColeccion::getHecho)
                .collect(Collectors.toList());
    }
}