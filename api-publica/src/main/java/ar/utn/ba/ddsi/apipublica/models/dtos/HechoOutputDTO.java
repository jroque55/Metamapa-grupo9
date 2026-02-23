package ar.utn.ba.ddsi.apipublica.models.dtos;

import ar.utn.ba.ddsi.apipublica.models.entities.EnumTipoHecho;
import ar.utn.ba.ddsi.apipublica.models.entities.Hecho;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class HechoOutputDTO {
    private Long id_hecho;
    private String titulo;
    private String descripcion;
    private String categoria;
    private String fecha;
    private String fechaDeCarga;
    private UbicacionOutputDTO ubicacion;
    private String etiqueta;
    private String tipoHecho;
    private String fuente; // nueva propiedad: fuente del hecho
    private List<AdjuntoDTO> adjuntos = new java.util.ArrayList<>();
    public HechoOutputDTO(Optional<Hecho> hecho) {
    }

    public HechoOutputDTO(Hecho hecho) {
        this.id_hecho= hecho.getId_hecho();
        this.titulo = hecho.getTitulo();
        this.descripcion = hecho.getDescripcion();
        //Estos != null ver si lo tengo que sacar
        this.categoria = hecho.getCategoria() !=null ?hecho.getCategoria().getNombre() : "";
        this.fecha = hecho.getFecha() != null ? hecho.getFecha().toString() : "";
        this.fechaDeCarga = hecho.getFechaDeCarga() != null ? hecho.getFechaDeCarga().toString() : "";
        this.etiqueta = hecho.getEtiqueta() != null ? hecho.getEtiqueta().getNombre(): null;
        this.ubicacion = hecho.getUbicacion() != null ? (new UbicacionOutputDTO( hecho.getUbicacion())): null;
        this.fuente = hecho.getFuente() !=null ? hecho.getFuente().getNombre(): "";
        if(hecho.getTipoHecho()!=null) {
            System.out.println("Tipo de hecho: " + hecho.getTipoHecho().name());
            this.tipoHecho = hecho.getTipoHecho().name();
        }else this.tipoHecho = EnumTipoHecho.TEXTO.name();
        if(hecho.getAdjuntos().size()>=1)hecho.getAdjuntos().forEach(adjunto -> this.adjuntos.add(new AdjuntoDTO(adjunto)));
    }
    @Override
    public String toString() {
        return "HechoOutputDTO{" +
                "titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", categoria='" + categoria + '\'' +
                ", fecha=" + fecha +
                ", fechaDeCarga=" + fechaDeCarga +
                ", etiqueta='" + etiqueta + '\'' +
                ", tipoHecho='" + tipoHecho + '\'' +
                ", fuente='" + (fuente!=null ? fuente : null) + '\'' +
                ", adjuntos=" + adjuntos +
                '}';
    }
}
