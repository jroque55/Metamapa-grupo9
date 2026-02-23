package utn.ddsi.agregador.domain.agregador;

import org.springframework.stereotype.Component;
import utn.ddsi.agregador.domain.condicion.CondicionFuente;
import utn.ddsi.agregador.domain.condicion.InterfaceCondicion;
import utn.ddsi.agregador.domain.hecho.Hecho;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class FiltradorDeHechos {


    public List<Hecho> devolverHechosAPartirDe(List<InterfaceCondicion> condiciones, List<Hecho> hechos) {
        if (hechos == null || hechos.isEmpty()) {
            return Collections.emptyList();
        }
         //hechos no nulos
        List<Hecho> base = hechos.stream().filter(Objects::nonNull).collect(Collectors.toList()); //filtra los nulls

        if (condiciones == null || condiciones.isEmpty()) {
            return new ArrayList<>(base); 
        }

        return base.stream().filter(hecho -> condiciones.stream().filter(Objects::nonNull).allMatch(cond -> cond.cumpleCondicion(hecho))).collect(Collectors.toList());
    }
    public List<Hecho> devolverHechosDeFuentes(List<Hecho> hechos, List<CondicionFuente> condicionesFuente){

        List<Hecho> base = hechos.stream().filter(Objects::nonNull).collect(Collectors.toList());
        return base.stream().filter(h-> perteneceAlaFuente(h,condicionesFuente)).collect(Collectors.toList());


    };

    public boolean perteneceAlaFuente(Hecho hecho, List<CondicionFuente> condiciones){
       return   condiciones.stream().anyMatch(f->hecho.getFuente().equals(f.getFuente()));

    }
    
}
