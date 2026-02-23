package utn.ddsi.agregador.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utn.ddsi.agregador.dto.EstadisticaColeccionHechosXProvinciaDTO;
import utn.ddsi.agregador.repository.IRepositoryHechoXColeccion;

import java.util.List;
//ver si manejar con service o solo con repo

@Service
public class ServiceHechoXColeccion {
    @Autowired
    private IRepositoryHechoXColeccion repohxc;

    public ServiceHechoXColeccion(IRepositoryHechoXColeccion repo){
        this.repohxc=repo;
    }

    public List<EstadisticaColeccionHechosXProvinciaDTO> contarHechosDeColeccionDeProvincia(Long id_coleccion){
        return this.repohxc.contarHechosDeColeccionDeProvincia(id_coleccion);
    }

}
