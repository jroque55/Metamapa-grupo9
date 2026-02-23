package ar.utn.ba.ddsi.fuenteproxy.models.dtos;

import ar.utn.ba.ddsi.fuenteproxy.models.entities.Hecho;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HechoOutputDTO {
    private String titulo;
    private String descripcion;
    private String categoria;
    private String fecha;
    private String fechaDeCarga;
    private String ubicacionLat; // latitud como string
    private String ubicacionLon; // longitud como string
    private String etiqueta;
    private String tipoHecho;
    private FuenteDTO fuente;
    private List<AdjuntoDTO> adjuntos;
    public HechoOutputDTO() {
    }

    public void HechoOutputDTO(Hecho hecho) {
        this.titulo = hecho.getTitulo();
        this.descripcion = hecho.getDescripcion();
        this.categoria = hecho.getCategoria().getNombre();
        this.fecha = hecho.getFecha().toString();
        this.fechaDeCarga = null;
        this.etiqueta = hecho.getEtiqueta() != null ? hecho.getEtiqueta().getNombre(): null;
        this.ubicacionLat = String.valueOf(hecho.getUbicacion().getLatitud());
        this.ubicacionLon = String.valueOf(hecho.getUbicacion().getLongitud());
        this.tipoHecho = hecho.getTipoHecho().name();
        this.fuente = new FuenteDTO(hecho.getFuente());
        hecho.getAdjuntos().forEach(adjunto -> this.adjuntos.add(new AdjuntoDTO(adjunto)));
    }

}