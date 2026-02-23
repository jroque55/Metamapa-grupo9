package ar.utn.ba.ddsi.fuenteproxy.models.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FuenteDemo extends FuenteProxy {
    private InterfaceConexion interfaceConexion;
    private List<Hecho> hechosCargados;

    public FuenteDemo(InterfaceConexion interfaceConexion, String url) {
        this.interfaceConexion = interfaceConexion;
        this.url = url; // Sets the parent's url field
        this.hechosCargados = new ArrayList<>();
    }

    @Override
    public List<Hecho> obtenerHechos() {
        return this.hechosCargados;
    }

    public void agregarHecho() {
        Map<String, Object> hechoObtenido = interfaceConexion.siguienteHecho(url);
        if( hechoObtenido != null) {
            Hecho hechoMapeado = construirHechoDesdeMapa(hechoObtenido);
            hechosCargados.add(hechoMapeado);
        }
        ultimaConsulta = LocalDateTime.now();
    }

    private Hecho construirHechoDesdeMapa(Map<String, Object> datos) {
        String titulo = (String) datos.get("titulo");
        String descripcion = (String) datos.get("descripcion");

        String categoriaStr = (String) datos.get("categoria");
        Categoria categoria = new Categoria(categoriaStr);

        String latitudStr = (String) datos.get("latitud");
        String longitudStr = (String) datos.get("longitud");
        float latitud = Float.parseFloat(latitudStr);
        float longitud = Float.parseFloat(longitudStr);
        Ubicacion ubicacion = new Ubicacion(latitud, longitud);

        String fechaStr = (String) datos.get("fecha");
        LocalDate fecha = LocalDate.parse(fechaStr);

        return new Hecho(titulo, descripcion, categoria, ubicacion, fecha);
    }
}
