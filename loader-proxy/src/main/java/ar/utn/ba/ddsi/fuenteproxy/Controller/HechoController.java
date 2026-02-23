package ar.utn.ba.ddsi.fuenteproxy.Controller;


import ar.utn.ba.ddsi.fuenteproxy.Service.IHechoServices;
import ar.utn.ba.ddsi.fuenteproxy.models.dtos.HechoOutputDTO;
import ar.utn.ba.ddsi.fuenteproxy.models.entities.Hecho;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


//Implementar el hecho de no bucar todos los hechos
//Cambiar el Local DAte a Local Date Time y así poder recibir por parametro hasta donde treaer
@RestController
@RequestMapping("/hechos")
public class HechoController { //Ver si esta bien el nombre

    @Autowired
    private IHechoServices hechoServices;
    @GetMapping
    public List<HechoOutputDTO> obtenerHechos() {
        System.out.println("Funciona el controller");
        return this.hechoServices.BuscarHechos();
    }
}
