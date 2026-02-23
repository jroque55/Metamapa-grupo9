package ar.utn.ba.ddsi.apipublica.controllers;

import ar.utn.ba.ddsi.apipublica.models.entities.Hecho;
import ar.utn.ba.ddsi.apipublica.models.repository.HechoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hechos-test")
public class HechoControllerTest {
    private final HechoRepository hechoRepository;

    public HechoControllerTest(HechoRepository hechoRepository) {
        this.hechoRepository = hechoRepository;
    }

    @PostMapping
    public ResponseEntity<Hecho> crear(@RequestBody Hecho h) {
        Hecho saved = hechoRepository.save(h);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Hecho>> listar() {
        return ResponseEntity.ok(hechoRepository.findAll());
    }
}

