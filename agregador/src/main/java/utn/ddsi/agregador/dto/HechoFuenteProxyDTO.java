package utn.ddsi.agregador.dto;

import jakarta.websocket.server.ServerEndpoint;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class HechoFuenteProxyDTO{
private String titulo;
private String descripcion;
private String categoria;
private String fecha;
private String fechaDeCarga;  //No debería tenerlo mepa, no sé jaja me parece que no
private String ubicacionLat; // latitud como string
private String ubicacionLon; // longitud como string
private String etiqueta;
private String tipoHecho;
private List<AdjuntoDTO> adjuntos = new java.util.ArrayList<>();

public HechoFuenteProxyDTO() {
};
}
