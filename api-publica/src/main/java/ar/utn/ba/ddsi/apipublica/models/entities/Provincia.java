package ar.utn.ba.ddsi.apipublica.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Provincia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_provincia;
    private String nombre;
    private String pais;
    public Provincia() {
    }

    public Provincia(String nombre, String pais) {}
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getPais() {
        return pais;
    }
    public void setPais(String pais) {
        this.pais = pais;
    }


}

