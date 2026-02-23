package ar.utn.ba.ddsi.apiadmi.servicies.interfaces;


import ar.utn.ba.ddsi.apiadmi.utils.EnumEstadoSol;
import ar.utn.ba.ddsi.apiadmi.models.entities.hecho.Hecho;

public interface IHechoService {

    void actualizarElEstadoDelHecho(Hecho hecho, EnumEstadoSol estado);

    void actualizarEtiqueta(Long id, String nombreEtiqueta);
}
