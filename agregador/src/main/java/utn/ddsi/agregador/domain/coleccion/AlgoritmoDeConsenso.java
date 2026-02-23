package utn.ddsi.agregador.domain.coleccion;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.jdbc.support.AbstractFallbackSQLExceptionTranslator;
import utn.ddsi.agregador.domain.fuentes.Fuente;
import utn.ddsi.agregador.domain.hecho.Hecho;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class AlgoritmoDeConsenso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_algoritmo;

    public boolean aplicar(EvidenciaDeHecho evidencia, int totalFuentes){
        return false;
    }
}

/** ver performance
 public Set<String> obtenerFuentesCoincidentes(
 HechoFuenteDTO hechoActual,
 List<HechoFuenteDTO> todosLosDatos,
 List<Fuente> fuentesColeccion
 ) {
 Set<String> urlsFuentesColeccion = fuentesColeccion.stream()
 .map(Fuente::getUrl)
 .collect(Collectors.toSet());

 // recolecta todas las fuentes de la colección donde aparece el hecho
 return todosLosDatos.stream()
 .filter(dto -> dto.getTitulo().equals(hechoActual.getTitulo()))
 .map(HechoFuenteDTO::getUrlFuente)
 .map(String::trim)  // evita errores por espacios
 .filter(urlsFuentesColeccion::contains)
 .collect(Collectors.toSet());
 }
 /*
 public boolean aplicarDTO(
 HechoFuenteDTO hechoActual,
 List<HechoFuenteDTO> todosLosDatosDeFuentes,
 List<Fuente> fuentesColeccion
 ) {
 System.out.println("aplicarDTO: hechoActual=" + hechoActual
 + ", todosSize=" + todosLosDatosDeFuentes.size()
 + ", fuentesSize=" + fuentesColeccion.size());
 Set<String> coincidencias =
 obtenerFuentesCoincidentes(hechoActual, todosLosDatosDeFuentes, fuentesColeccion);

 return !coincidencias.isEmpty();
 }*/