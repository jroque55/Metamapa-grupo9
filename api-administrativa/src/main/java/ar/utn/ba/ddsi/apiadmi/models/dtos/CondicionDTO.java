package ar.utn.ba.ddsi.apiadmi.models.dtos;

import ar.utn.ba.ddsi.apiadmi.servicies.CondicionService;
import lombok.Data;

@Data
public class CondicionDTO {
    private Long id;
    private String tipo;
    private String valor;

    public CondicionDTO(Long id, String tipo , String valor ){
        this.id = id;
        this.tipo = tipo;
        this.valor = valor;
    }
}


