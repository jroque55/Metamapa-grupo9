package ar.utn.ba.ddsi.fuenteproxy.models.entities;

public class Provincia {
    private long idProvincia;
    private String nombre;
    private String pais;

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
