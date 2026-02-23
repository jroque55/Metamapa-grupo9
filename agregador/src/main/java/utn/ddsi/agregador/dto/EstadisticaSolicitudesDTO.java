package utn.ddsi.agregador.dto;

import lombok.Data;

@Data
public class EstadisticaSolicitudesDTO {

    private Long cantSolicitudes;
    private Long cantSpam;


    public EstadisticaSolicitudesDTO(Long cantidad, Long spam){
        this.cantSpam = spam;
        this.cantSolicitudes = cantidad;
    }

}
