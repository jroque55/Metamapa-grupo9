package ar.utn.ba.ddsi.apiadmi.servicies.interfaces;

import ar.utn.ba.ddsi.apiadmi.models.entities.fuente.Fuente;

import java.util.List;

public interface IFuenteServices {
    public Fuente buscarPorId(Long id);
    List<Fuente> obtenerFuentes();

    Fuente buscarPorNombre(String nombre);
}
