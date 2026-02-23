package ar.utn.ba.ddsi.apipublica.models.entities;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class FiltradorDeHechos {
    
    //PROBAR FILTRADOR (singleton Spring)
    public List<Hecho> devolverHechosAPartirDe(List<InterfaceCondicion> condiciones, List<Hecho> hechos) {
        if (hechos == null || hechos.isEmpty()) {
            return Collections.emptyList();
        }
        //Consulta: filtra los nulls hace falta?
        List<Hecho> base = hechos.stream().filter(Objects::nonNull).toList();

        if (condiciones == null || condiciones.isEmpty()) {
            return new ArrayList<>(base);
        }

        return base.stream()
                .filter(hecho -> condiciones.stream()
                        .filter(Objects::nonNull)
                        .allMatch(cond -> cond.cumpleCondicion(hecho)))
                .toList();
    }
    
}
