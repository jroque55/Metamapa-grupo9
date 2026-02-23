package ar.utn.ba.ddsi.fuenteproxy.Service;

import ar.utn.ba.ddsi.fuenteproxy.models.dtos.HechoOutputDTO;
import ar.utn.ba.ddsi.fuenteproxy.models.repository.IFuenteRepository;
import ar.utn.ba.ddsi.fuenteproxy.models.entities.*;
import ar.utn.ba.ddsi.fuenteproxy.models.factory.FactoryFuenteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class HechoServices implements IHechoServices {
    private List<FuenteProxy> fuenteProxis = new ArrayList<>();
    private Long ultimoId = (long) 0; //RARIIISIMO,VER ESTO

    private FactoryFuenteProxy factoryFuenteProxy = FactoryFuenteProxy.getInstance();
    @Autowired
    private IFuenteRepository fuenteRepository ;

    @Override
    public List<HechoOutputDTO> BuscarHechos() {
        System.out.println("Buscando Hechos");
        BuscarNuevasFuentes();
        System.out.println("Fuentes Proxy cargadas: " + this.fuenteProxis.size());
        List<Hecho> hechosObtenidos = new ArrayList<>();
        this.fuenteProxis.forEach(fuenteProxy -> hechosObtenidos.addAll(ObtenerHechosDeFuente(fuenteProxy)));
        System.out.println("Hechos Obtenidos: " + hechosObtenidos.size());
        return pasarAHechosOuputDTO(hechosObtenidos);
    }
    public List<Hecho> ObtenerHechosDeFuente(FuenteProxy fuenteProxy) {
        System.out.println("Obteniendo Hechos de Fuentes");
        List<Hecho> hechosObtenidos = fuenteProxy.obtenerHechos();
        Fuente fuente = this.fuenteRepository.findByUrl(fuenteProxy.getUrl());
        hechosObtenidos.forEach(hecho -> {
            hecho.setFuente(fuente);
        });
        return hechosObtenidos;
    }

    public List<HechoOutputDTO> pasarAHechosOuputDTO(List<Hecho> hechos) {
        List<HechoOutputDTO> hechoOutputDTOs = new ArrayList<>();
        hechos.forEach(hecho -> {
            HechoOutputDTO dto = new HechoOutputDTO();
            dto.HechoOutputDTO(hecho);
            hechoOutputDTOs.add(dto);
        });
        return hechoOutputDTOs;
    }
    @Override
    public void BuscarNuevasFuentes() {
        System.out.println("Buscando Nuevas Fuentes");
        //SOLOPORAHORA MEJORAR TODOESTO XD
        List<Fuente> fuentes = this.fuenteRepository.findByIdFuenteGreaterThan(this.ultimoId);
        fuentes.forEach(fuente -> {
                    System.out.println(fuente.getId_fuente());
                    System.out.println(fuente.getNombre());
                    System.out.println(fuente.getTipoFuente());
                    this.ultimoId +=1;
                });
        fuentes.forEach(fuente -> {
            if(fuente.getTipoFuente().name().equalsIgnoreCase("METAMAPA")){
                this.fuenteProxis.add(this.factoryFuenteProxy.createFuenteProxy(fuente));
            }

        });
        //PRUEBA
        this.fuenteProxis.forEach(fuenteProxy ->{
                System.out.println("Fuente Proxy agregada: " + fuenteProxy.toString());

            }
        );
    }

}
