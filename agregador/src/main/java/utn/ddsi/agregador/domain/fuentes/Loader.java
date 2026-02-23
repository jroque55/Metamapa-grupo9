package utn.ddsi.agregador.domain.fuentes;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.Setter;
import utn.ddsi.agregador.adapter.HechoAdapter;
import utn.ddsi.agregador.domain.hecho.Hecho;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public abstract class Loader {
    private String  ruta;
    private HechoAdapter adapter;

    public Loader() {}

    public abstract List<Hecho> obtenerHechos();
    }

