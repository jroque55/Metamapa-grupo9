package utn.ddsi.agregador.domain.solicitudEliminacion;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DetectorBasicoDeSpam implements DetectorDeSpam {

    private static final List<String> palabrasSpam = Arrays.asList(
        "dinero", "gratis", "oferta", "haz clic", "urgente", "ganaste", "premio", "!!!"
    ); 

    @Override
    public boolean esSpam(String texto) {
        if (texto == null || texto.trim().length() < 10) {
            return true; // spam si es muy corto
        }

        String textoMin = texto.toLowerCase();

        return palabrasSpam.stream().anyMatch(textoMin::contains);
    }
}