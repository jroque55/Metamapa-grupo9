package ar.utn.ba.ddsi.apiadmi.servicies;

import ar.utn.ba.ddsi.apiadmi.models.entities.condiciones.InterfaceCondicion;
import ar.utn.ba.ddsi.apiadmi.models.repository.ICriteriosRepository;
import ar.utn.ba.ddsi.apiadmi.servicies.interfaces.ICondicionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CondicionService implements ICondicionService {

    @Autowired
    private ICriteriosRepository repo;


    @Override
    public InterfaceCondicion buscarPorId(Long id) {
        InterfaceCondicion condicion = this.repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Condicion no encontrada: " + id));
        return condicion;
    }

    public List<InterfaceCondicion> findAll() {
        return repo.findAll();
    }
    public void deleteBy(Long id) {
        repo.deleteById(id);
    }
}

