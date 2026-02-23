package utn.ddsi.agregador.domain.coleccion;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utn.ddsi.agregador.domain.condicion.InterfaceCondicion;
import utn.ddsi.agregador.domain.fuentes.Fuente;
import utn.ddsi.agregador.domain.hecho.Hecho;
import utn.ddsi.agregador.utils.EnumTipoDeAlgoritmo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter 
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
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
    private List<HechoXColeccion> hechos = new ArrayList<>();
    @ManyToMany
    @JoinTable(
            name = "condicion_x_coleccion",
            joinColumns = @JoinColumn(name = "id_coleccion"),
            inverseJoinColumns = @JoinColumn(name = "id_condicion")
    )
    private List<InterfaceCondicion> condicionDePertenencia = new ArrayList<>();
    @Transient
    private AlgoritmoDeConsenso algoritmoDeConsenso;
    @Enumerated(EnumType.STRING)
    private EnumTipoDeAlgoritmo tipoDeAlgoritmo;

    public Coleccion(String titulo, String descripcion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fuentes = new ArrayList<>();
        this.hechos = new ArrayList<>();
        this.tipoDeAlgoritmo = EnumTipoDeAlgoritmo.DEFAULT;
        this.algoritmoDeConsenso = new ConsensoDefault();
    }

    @PostLoad
    private void cargarAlgoritmoDeConsenso() {
        if (tipoDeAlgoritmo != null) {
            this.algoritmoDeConsenso = obtenerAlgoritmoPorTipo(tipoDeAlgoritmo);
        } else {
            this.algoritmoDeConsenso = new ConsensoDefault();
        }
    }

    private AlgoritmoDeConsenso obtenerAlgoritmoPorTipo(EnumTipoDeAlgoritmo tipo) {
        switch (tipo) {
            case ABSOLUTA:
                return new ConsensoAbsoluto();
            case DEFAULT:
            default:
                return new ConsensoDefault();
            case MULTIPLES_MENCIONES:
                 return new MencionesMultiples();
            case MAYORIA_SIMPLE:
                return new MayoriaSimple();
        }
    }

//    public void setearFuente() {
//        this.hechos.forEach((hxc) -> hxc.getHecho().setFuente(this.fuentes.get(0)));
//    }
//      public List<Hecho> obtenerHechosConsensuados(List<Fuente> fuentes){ //hacer otro map 0 no consensuado 1 consensuado
//      List<Hecho> hechosConsensuados=this.algoritmoDeConsenso.aplicar(this.hechos,fuentes);
//        return hechosConsensuados
//  }
/*
    public void aplicarConsenso(HechoXColeccion hxc, EvidenciaDeHecho evidencia) {
        if (algoritmoDeConsenso.aplicar(evidencia, fuentes.size())) {
            hxc.setConsensuado(Boolean.TRUE);
        }
    }
    // Metodo para cambiar el tipo de algoritmo (actualiza tanto el enum como la instancia)
    public void setTipoDeAlgoritmo(EnumTipoDeAlgoritmo tipoDeAlgoritmo) {
        this.tipoDeAlgoritmo = tipoDeAlgoritmo;
        this.algoritmoDeConsenso = obtenerAlgoritmoPorTipo(tipoDeAlgoritmo);
    }
    public void agregarHechos(List<Hecho> nuevosHechos) {
        nuevosHechos.forEach(hecho -> {
            HechoXColeccion hxc = new HechoXColeccion(hecho, this, false);
            this.hechos.add(hxc);
        });
    }

    public void agregarCriterioDePertenencia(InterfaceCondicion criterio) {
        condicionDePertenencia.add(criterio);
    }
    // Metodo para obtener solo los hechos consensuados (para navegación curada)
    public List<Hecho> obtenerSoloHechosConsensuados() {
        return hechos.stream()
                .filter(HechoXColeccion::getConsensuado)
                .map(HechoXColeccion::getHecho)
                .collect(Collectors.toList());
    }

    // Metodo para obtener todos los hechos (para navegación no curada)
    public List<Hecho> obtenerTodosLosHechos() {
        return hechos.stream()
                .map(HechoXColeccion::getHecho)
                .collect(Collectors.toList());
    }
*/
}
