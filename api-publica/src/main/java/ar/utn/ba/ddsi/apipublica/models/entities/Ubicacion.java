package ar.utn.ba.ddsi.apipublica.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Ubicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_ubicacion;
    private float latitud;
    private float longitud;
    @ManyToOne
    @JoinColumn(name = "id_provincia")
    private Provincia provincia;

    public Ubicacion() {
    }

    public Ubicacion(float latitud, float longitud,Provincia provincia) {
        if(this.id_ubicacion ==0){
            this.id_ubicacion =1;
        }
        this.latitud = latitud;
        this.longitud = longitud;
        this.provincia=provincia;
    }

    public void setUbicacion(float newLatitud, float newLongitud) {
        this.latitud = newLatitud;
        this.longitud = newLongitud;
    }

    public float getLatitud() {return this.latitud;}

    public float getLongitud() {
        return longitud;
    }
    public long getIdUbicacion() {return this.id_ubicacion;}
}