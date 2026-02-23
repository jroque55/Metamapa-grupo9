package ar.utn.ba.ddsi.fuenteproxy.models.factory;

import java.util.Collections;

import ar.utn.ba.ddsi.fuenteproxy.models.entities.InterfaceConexion;
import ar.utn.ba.ddsi.fuenteproxy.models.entities.FuenteDemo;
import ar.utn.ba.ddsi.fuenteproxy.models.entities.FuenteMetamapa;
import ar.utn.ba.ddsi.fuenteproxy.models.entities.FuenteProxy;
import ar.utn.ba.ddsi.fuenteproxy.models.entities.Fuente;
import ar.utn.ba.ddsi.fuenteproxy.models.entities.EnumTipoFuente;
import org.springframework.web.reactive.function.client.WebClient;

public class FactoryFuenteProxy {

   //Todod es del constructor es demasiado al dope?
    private static FactoryFuenteProxy instance;
    private FactoryFuenteProxy() {
    }
    public static FactoryFuenteProxy getInstance() {
        if (instance == null) {
            synchronized (FactoryFuenteProxy.class) {
                if (instance == null) {
                    instance = new FactoryFuenteProxy();
                }
            }
        }
        return instance;
    }
    // Nuevo método que recibe la entidad persistida y delega según el tipo
    public FuenteProxy createFuenteProxy(Fuente fuente) {
        if (fuente == null) {
            throw new IllegalArgumentException("Fuente no puede ser null");
        }
        EnumTipoFuente tipo = fuente.getTipoFuente();
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de fuente no definido en la entidad Fuente");
        }
        String url = fuente.getUrl();
        String nombre = fuente.getNombre();
        String tipoFuenteStr = tipo.toString();
        switch (tipo) {
            case METAMAPA:
                return createFuenteMetamapa(url);
            case DEMO:
                return createFuenteDemo(url);
            case ESTATICA:
            default:
                throw new UnsupportedOperationException("Tipo de fuente no soportado: " + tipo);
        }
    }

    // Métodos auxiliares: crean instancias concretas a partir de la URL
    private FuenteDemo createFuenteDemo(String url) {
        //Funca???
        //Que le tengo que mandar a conexión?
        InterfaceConexion interfaceConexion = (u) -> Collections.emptyMap();
        return new FuenteDemo(interfaceConexion, url);
    }
    //Porque son metodos private??
    private FuenteMetamapa createFuenteMetamapa(String url) {
        WebClient.Builder webClientBuilder = WebClient.builder();
        return new FuenteMetamapa(url, webClientBuilder);
    }
}
