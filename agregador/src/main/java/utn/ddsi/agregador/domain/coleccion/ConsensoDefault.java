package utn.ddsi.agregador.domain.coleccion;
import utn.ddsi.agregador.domain.fuentes.Fuente;
import utn.ddsi.agregador.domain.hecho.Hecho;

import java.util.List;
import java.util.stream.Collectors;

public class ConsensoDefault extends AlgoritmoDeConsenso {
    @Override
    public boolean aplicar(EvidenciaDeHecho evidencia, int totalFuentes){
        return true;
    }
}
