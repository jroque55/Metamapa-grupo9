package ar.utn.ba.ddsi.apiadmi.models.factory;

import ar.utn.ba.ddsi.apiadmi.models.dtos.input.ColeccionInput;
import ar.utn.ba.ddsi.apiadmi.models.entities.coleccion.Coleccion;
import org.springframework.stereotype.Component;

@Component
public class ColeccionFactory {



    public Coleccion crearColeccion(ColeccionInput coleccion){

        Coleccion cole = new Coleccion();
        cole.setTitulo(coleccion.getTitulo());
        cole.setDescripcion(coleccion.getDescripcion());
        // NOTA: las fuentes y criterios se setean después desde el Service
        return cole;
    }
}
