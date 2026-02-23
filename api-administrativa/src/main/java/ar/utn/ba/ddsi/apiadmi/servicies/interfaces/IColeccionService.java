package ar.utn.ba.ddsi.apiadmi.servicies.interfaces;

import ar.utn.ba.ddsi.apiadmi.models.dtos.ColeccionDto;
import ar.utn.ba.ddsi.apiadmi.models.dtos.input.ColeccionInput;
import ar.utn.ba.ddsi.apiadmi.models.entities.coleccion.Coleccion;


import java.util.List;
public interface IColeccionService {
    public List<ColeccionDto> obtenerColecciones();
    public void agregar(ColeccionInput coleccion);
    public void actualizar(Long id,ColeccionInput coleccion);
    public void eliminar(Long idColeccion);
    public Coleccion encontrarPorId(Long id);
}
