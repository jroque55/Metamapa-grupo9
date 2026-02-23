package ar.utn.ba.ddsi.fuenteproxy.models.entities;

public class Ubicacion {
    private long id_Ubicacion;
    private float latitud;
    private float longitud;
    private Provincia provincia;

    public Ubicacion(float latitud, float longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Ubicacion(float latitud, float longitud, Provincia provincia) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.provincia = provincia;
    }

    public void setUbicacion(float newLatitud, float newLongitud) {
        this.latitud = newLatitud;
        this.longitud = newLongitud;
    }

    public float getLatitud() {return this.latitud;}

    public float getLongitud() {
        return longitud;
    }
}
