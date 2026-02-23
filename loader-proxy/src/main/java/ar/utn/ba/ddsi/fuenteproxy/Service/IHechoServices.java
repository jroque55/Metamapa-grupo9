package ar.utn.ba.ddsi.fuenteproxy.Service;

import ar.utn.ba.ddsi.fuenteproxy.models.dtos.HechoOutputDTO;
import ar.utn.ba.ddsi.fuenteproxy.models.entities.Hecho;

import java.util.List;

public interface IHechoServices {
    public List<HechoOutputDTO> BuscarHechos();
    public void BuscarNuevasFuentes();
}
