package utn.ddsi.agregador.domain.coleccion;

import utn.ddsi.agregador.domain.fuentes.Fuente;
import utn.ddsi.agregador.domain.hecho.Hecho;

import java.util.*;
import java.util.stream.Collectors;

public class MencionesMultiples extends AlgoritmoDeConsenso {
    @Override
    public boolean aplicar(EvidenciaDeHecho e, int totalFuentes) {
        return !e.hayConflicto() && e.getFuentesQueLoMencionan().size() >= 2;
    }
}