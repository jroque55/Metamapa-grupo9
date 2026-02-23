package ar.utn.ba.ddsi.fuenteproxy.models.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class PageResponseDTO<T> {
    private List<T> content;
}
