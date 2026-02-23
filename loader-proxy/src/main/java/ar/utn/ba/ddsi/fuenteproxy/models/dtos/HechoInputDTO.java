package ar.utn.ba.ddsi.fuenteproxy.models.dtos;

import ar.utn.ba.ddsi.fuenteproxy.models.entities.Adjunto;
import ar.utn.ba.ddsi.fuenteproxy.models.entities.Hecho;
import ar.utn.ba.ddsi.fuenteproxy.models.entities.Ubicacion;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HechoInputDTO {
    private Long id_hecho;
    private String titulo;
    private String descripcion;
    private String categoria;
    private String fecha;
    private String fechaDeCarga;
    private UbicacionInputDTO ubicacion;
    private String etiqueta;
    private String tipoHecho;
    private String fuente;
    private List<AdjuntoDTO> adjuntos = new java.util.ArrayList<>();

    public HechoInputDTO() {
    }
}

