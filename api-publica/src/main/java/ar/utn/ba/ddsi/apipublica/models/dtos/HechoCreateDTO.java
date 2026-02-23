package ar.utn.ba.ddsi.apipublica.models.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class HechoCreateDTO {
    @NotBlank
    private String titulo;
    @NotBlank
    private String descripcion;@NotBlank
    @NotBlank
    private String fecha; // ISO yyyy-MM-dd
    @NotBlank
    private String categoria; // id o nombre
    @NotBlank
    private String fuente; // id
    @NotBlank
    private String ubicacionLat; // latitud como string
    @NotBlank
    private String ubicacionLon; // longitud como string
    @NotBlank
    private String tipoHecho; // enum name
    private List<String> adjuntos; // lista de urls

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getFuente() { return fuente; }
    public void setFuente(String fuente) { this.fuente = fuente; }

    public String getUbicacionLat() { return ubicacionLat; }
    public void setUbicacionLat(String ubicacionLat) { this.ubicacionLat = ubicacionLat; }

    public String getUbicacionLon() { return ubicacionLon; }
    public void setUbicacionLon(String ubicacionLon) { this.ubicacionLon = ubicacionLon; }

    public String getTipoHecho() { return tipoHecho; }
    public void setTipoHecho(String tipoHecho) { this.tipoHecho = tipoHecho; }

    public List<String> getAdjuntos() { return adjuntos; }
    public void setAdjuntos(List<String> adjuntos) { this.adjuntos = adjuntos; }

}
