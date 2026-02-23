package utn.ddsi.agregador.domain.coleccion;

import utn.ddsi.agregador.domain.fuentes.Fuente;
import utn.ddsi.agregador.domain.hecho.Hecho;

import java.util.*;
import java.util.stream.Collectors;

public class MayoriaSimple extends AlgoritmoDeConsenso {
    @Override
    public boolean aplicar(EvidenciaDeHecho e, int totalFuentes) {
        int minimo = (int) Math.ceil(totalFuentes / 2.0);
        return e.getFuentesQueLoMencionan().size() >= minimo;
    }
}
/*
public List<Hecho> aplicar(List<HechoXColeccion>hechos, List<Fuente> fuentes){
// Mapa para contar las menciones de cada hecho
Map<Hecho, Integer> mapa = new HashMap<>();
List<Hecho> hechosConsensuados = new ArrayList<>();
// Contar menciones
for (HechoXColeccion h : hechos) {
 mapa.put(h.getHecho(), mapa.getOrDefault(h, 0) + 1);
  }
  // Filtrar por al menos la mitad de las fuentes
  for (Map.Entry<Hecho, Integer> entry : mapa.entrySet())
   { if (entry.getValue() > fuentes.size()/2) { hechosConsensuados.add(entry.getKey()); }
   }
   return hechosConsensuados;
    // } */