package ar.utn.ba.ddsi.fuenteproxy.models.entities;

import jakarta.persistence.*;

@Entity
public class Fuente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_fuente;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String url;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumTipoFuente tipoFuente;

    public Fuente(long id, String nombre, String url, EnumTipoFuente tipoFuente) {
        this.id_fuente = id;
        this.nombre = nombre;
        this.url = url;
        this.tipoFuente = tipoFuente;
    }
    public Fuente(String nombre, String url, EnumTipoFuente tipoFuente) {
        this.nombre = nombre;
        this.url = url;
        this.tipoFuente = tipoFuente;
    }

    public Fuente() {
    }
    public long getId_fuente() {
        return id_fuente;
    }
    public void setId_fuente(long id_fuente) {
        this.id_fuente = id_fuente;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getUrl() {
        return url;}
    public void setUrl(String url) {
        this.url = url;
    }
    public EnumTipoFuente getTipoFuente() {
        return tipoFuente;
    }
    public void setTipoFuente(EnumTipoFuente tipoFuente) {
        this.tipoFuente = tipoFuente;}

}
