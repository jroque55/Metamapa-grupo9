package utn.ddsi.agregador.dto;

import lombok.Getter;

public record MencionDeHecho(
        Long hechoId,
        Long fuenteId,
        String descripcion
) {}