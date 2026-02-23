package utn.ddsi.agregador.domain.coleccion;

import lombok.Getter;

import java.util.Collections;
import java.util.Set;

//EVIDENCIA: para cada hecho, me dice las fuentes que lo mencionan,
//CONFLICTO: si las descripciones son distintas, solo para multiples menciones
public class EvidenciaDeHecho {

    @Getter
    private final Long hechoId;
    @Getter
    private final Set<Long> fuentesQueLoMencionan;
    @Getter
    private final boolean hayConflicto;

    public EvidenciaDeHecho(
            Long hechoId,
            Set<Long> fuentesQueLoMencionan,
            boolean hayConflicto
    ) {
        this.hechoId = hechoId;
        this.fuentesQueLoMencionan = fuentesQueLoMencionan;
        this.hayConflicto = hayConflicto;
    }
    public Boolean hayConflicto() {
        return hayConflicto;
    }
    //no hay evidencia de la fuente
    public static EvidenciaDeHecho vacia(Long hechoId) {
        return new EvidenciaDeHecho(
                hechoId,
                Collections.emptySet(),
                false
        );
    }

}