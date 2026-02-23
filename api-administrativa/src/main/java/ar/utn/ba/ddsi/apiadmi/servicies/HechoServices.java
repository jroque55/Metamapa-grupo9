package ar.utn.ba.ddsi.apiadmi.servicies;

import ar.utn.ba.ddsi.apiadmi.models.entities.hecho.Etiqueta;
import ar.utn.ba.ddsi.apiadmi.models.repository.IEtiquetaRepository;
import ar.utn.ba.ddsi.apiadmi.utils.EnumEstadoHecho;
import ar.utn.ba.ddsi.apiadmi.utils.EnumEstadoSol;
import ar.utn.ba.ddsi.apiadmi.models.entities.hecho.Hecho;
import ar.utn.ba.ddsi.apiadmi.models.repository.IHechosRepository;
import ar.utn.ba.ddsi.apiadmi.servicies.interfaces.IHechoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class HechoServices implements IHechoService {

    @Autowired
    private IHechosRepository hechoRepo;
    @Autowired
    private IEtiquetaRepository etiquetaRepo;

    @Autowired
    public HechoServices(IHechosRepository hechoRepo, IEtiquetaRepository etiquetaRepo) {
        this.hechoRepo = hechoRepo;
        this.etiquetaRepo = etiquetaRepo;
    }
    @Override
    public void actualizarElEstadoDelHecho(Hecho hecho, EnumEstadoSol estado) {

        if (estado == EnumEstadoSol.ACEPTADA) {
            hecho.setEstado(EnumEstadoHecho.BAJA);
            this.hechoRepo.save(hecho);
        }
    }
    @Override
    public void actualizarEtiqueta(Long idHecho, String nombreEtiqueta) {

        log.info("Obteniendo hecho con ID: {}", idHecho);
        Hecho hecho = hechoRepo.findById(idHecho)
                .orElseThrow(() -> {
                    log.error("Hecho con ID {} no encontrado", idHecho);

                    return new RuntimeException("Hecho no encontrado");});

        // Normalización de etiqueta
        String normalizada = nombreEtiqueta.trim().toUpperCase();

        // Buscar etiqueta existente o crear nueva
        Etiqueta etiqueta = etiquetaRepo.findByNombreIgnoreCase(normalizada)
                .orElseGet(() -> {
                    log.warn("Etiqueta '{}' no encontrada, creando nueva", normalizada);
                    Etiqueta nueva = new Etiqueta();
                    nueva.setNombre(normalizada);
                    return etiquetaRepo.save(nueva);
                });

        // Asociar etiqueta al hecho
        hecho.setEtiqueta(etiqueta);
        log.info("Etiqueta '{}' asociada al hecho ID {}", etiqueta.getNombre(), idHecho);
        // Guardar (no returns)
        hechoRepo.save(hecho);

    }

}
