package utn.ddsi.agregador.domain.agregador;

import java.util.*;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import utn.ddsi.agregador.domain.coleccion.Coleccion;
import utn.ddsi.agregador.domain.coleccion.EvidenciaDeHecho;
import utn.ddsi.agregador.domain.coleccion.HechoXColeccion;
import utn.ddsi.agregador.domain.condicion.CondicionFuente;
import utn.ddsi.agregador.domain.condicion.InterfaceCondicion;
import utn.ddsi.agregador.domain.fuentes.Fuente;
import utn.ddsi.agregador.domain.fuentes.Loader;
import utn.ddsi.agregador.domain.hecho.Hecho;
import utn.ddsi.agregador.domain.solicitudEliminacion.GestorDeSolicitudes;
import utn.ddsi.agregador.dto.MencionDeHecho;
import utn.ddsi.agregador.repository.IRepositoryColecciones;
import utn.ddsi.agregador.repository.IRepositoryFuentes;
import utn.ddsi.agregador.repository.IRepositoryHechoXColeccion;
import utn.ddsi.agregador.repository.IRepositoryHechos;
import utn.ddsi.agregador.utils.EnumEstadoHecho;

@Slf4j
@Data
@Component
public class ActualizadorColecciones {

    private List<Loader> loaders;
    private final IRepositoryColecciones repositoryColecciones;
    private final IRepositoryHechos repositoryHechos;
    private final Normalizador normalizador;
    private final GestorDeSolicitudes gestorSolicitudes;
    public final FiltradorDeHechos filtradorDeHechos;
    public final IRepositoryHechoXColeccion repoHechoxColeccion;
    public final IRepositoryFuentes repositoryFuente;

    public ActualizadorColecciones(IRepositoryColecciones rcole, IRepositoryHechos rhechos, Normalizador normal, GestorDeSolicitudes gestor, List<Loader> loaders, FiltradorDeHechos filtrador, IRepositoryHechoXColeccion repositoryHechoXColeccion, IRepositoryFuentes repositoryFuente) {
        this.repositoryColecciones = rcole;
        this.repositoryHechos = rhechos;
        this.gestorSolicitudes = gestor;
        this.normalizador = normal;
        this.loaders = loaders;
        this.filtradorDeHechos = filtrador;
        this.repoHechoxColeccion = repositoryHechoXColeccion;
        this.repositoryFuente = repositoryFuente;
    }


    public List<Hecho> traerHechosDeLoaders(){

        log.info("Iniciando obtención de hechos desde {} loaders: {}",
                loaders.size(),
                loaders.stream().map(l -> l.getClass().getSimpleName()).toList());

        List<Hecho> hechosNuevos = new ArrayList<>();
        for(Loader loader : loaders){

            try {List<Hecho> hechosObtenidos = loader.obtenerHechos();
                log.info("Loader {}: {} hechos obtenidos", loader.getClass().getSimpleName(), hechosObtenidos.size());
                hechosNuevos.addAll(hechosObtenidos);
                //Falta poner lo de la horas según corresponda al Loader
            } catch (Exception e) {
                log.error("Error al obtener hechos del loader {}: {}", loader.getClass().getSimpleName(), e.getMessage(), e);
                // ignorar loader con error y seguir con los siguientes
            }
            }
        log.info("Total de hechos obtenidos de todos los loaders: {}", hechosNuevos.size());
        return hechosNuevos;
    }

    public List<Hecho> depurarHechos() {
        log.debug("Iniciando depuración y normalización de hechos");
        List<Hecho> todosLosHechos = traerHechosDeLoaders();
        if (todosLosHechos == null || todosLosHechos.isEmpty()) return Collections.emptyList();
        List<Hecho> hechosNormalizados = normalizador.normalizar(todosLosHechos);
        log.debug("Hechos normalizados: {}", hechosNormalizados.size());
        // guardar solo los nuevos (ejemplo si existe getExternalId)
        List<Hecho> aGuardar = new ArrayList<>();
        for (Hecho h : hechosNormalizados) {

            Hecho hechoguardado =repositoryHechos.findFirstByTitulo(h.getTitulo());
            if (hechoguardado == null ||
                    !hechoguardado.getFuente().getId_fuente().equals(h.getFuente().getId_fuente())) {
                h.setEstado(EnumEstadoHecho.ALTA);
                aGuardar.add(h);
            }
        }
        log.info("Intentando guardar {} hechos nuevos", aGuardar.size());
        if (!aGuardar.isEmpty()) {
            for (Hecho h : aGuardar) {
                log.info("  -> Guardando hecho: {} | Fuente: {} | Ubicacion: {}",
                    h.getTitulo(),
                    h.getFuente() != null ? h.getFuente().getNombre() : "null",
                    h.getUbicacion() != null ? "lat=" + h.getUbicacion().getLatitud() : "null");
            }
        }
        repositoryHechos.saveAll(aGuardar);
        log.info("Se guardaron {} nuevos hechos", aGuardar.size());
        return hechosNormalizados;
    }


    @Transactional
    public void actualizarColecciones(){

        log.info("Iniciando actualización de colecciones");
        depurarHechos(); //En este punto ya guardé los nuevos porloque en el findAll siguiente los trae
        List<Hecho> hechosTotales = repositoryHechos.findAll();
        List<Coleccion> colecciones = repositoryColecciones.findAll();
        List<Hecho> hechosFiltradosPorFuente;
        List<CondicionFuente> condicionesFuentes;
        List<Fuente> fuentesColeccion;

        // la consigna pide: Este servicio utiliza el mecanismo de rechazos de solicitudes
        // de eliminación spam en forma automática definido en la Entrega 2
        //gestorSolicitudes.procesarTodasLasSolicitudes(); AHORA SE HACE SOLO CON SU PROPIO SCHEDULER

        for (Coleccion coleccion : colecciones) {
            // obtener hechos en la coleccion y condiciones de pertenencia
            List<HechoXColeccion> hechosEnCol = this.repoHechoxColeccion.findByColeccion(coleccion.getId_coleccion());
            List<InterfaceCondicion> condiciones = this.repositoryColecciones.findByIdCondiciones(coleccion.getId_coleccion());

            // obtener fuentes y condiciones de fuentes (una sola vez)
            fuentesColeccion = this.repositoryFuente.findFuentesByColeccion(coleccion.getId_coleccion());
            condicionesFuentes = crearCondicionesDeFuentes(fuentesColeccion);

            // 1) Eliminar hechos que ya no cumplen
            eliminarHechosNoValidos(hechosEnCol, condiciones, condicionesFuentes, hechosTotales);

            // 2) Determinar hechos válidos a partir de todas las fuentes y condiciones
            hechosFiltradosPorFuente = filtradorDeHechos.devolverHechosDeFuentes(hechosTotales, condicionesFuentes);
            List<Hecho> hechosFiltrados = filtradorDeHechos.devolverHechosAPartirDe(condiciones, hechosFiltradosPorFuente);

            // 3) Insertar los que falten (sólo los que no existan)
            for (Hecho h : hechosFiltrados) {
                List<HechoXColeccion> hxcList = this.repoHechoxColeccion.findByConjunto(coleccion.getId_coleccion(), h.getId_hecho());
                if (hxcList.isEmpty()) {
                    // Si en la coleccion ya existe un hecho con mismo titulo y estado BAJA,
                    // marcar el nuevo hecho como BAJA antes de guardarlo
                    aplicarEstadoBajaSiCorresponde(h, coleccion.getId_coleccion(), hechosEnCol);

                    HechoXColeccion nuevo = new HechoXColeccion(h, coleccion, false);
                    this.repoHechoxColeccion.save(nuevo);
                }
            }
        }


        repositoryColecciones.saveAll(colecciones);
        log.info("Actualización de colecciones finalizada");
    }


    // Nuevo helper: si existe en la coleccion un hecho con el mismo título y estado BAJA,
    // marcar el hecho dado como BAJA y persistir el cambio.
    private void aplicarEstadoBajaSiCorresponde(Hecho hecho, Long idColeccion, List<HechoXColeccion> hechosEnCol){
        if(hecho == null || hecho.getTitulo() == null) return;
        List<HechoXColeccion> lista = hechosEnCol;
        if(lista == null) lista = this.repoHechoxColeccion.findByColeccion(idColeccion);
        for(HechoXColeccion hxc : lista){
            if(hxc == null || hxc.getHecho() == null) continue;
            Hecho existente = hxc.getHecho();
            if(existente.getTitulo() != null && existente.getTitulo().equals(hecho.getTitulo())){
                if(existente.getEstado() != null && existente.getEstado().equals(EnumEstadoHecho.BAJA)){
                    hecho.setEstado(EnumEstadoHecho.BAJA);
                    // guardar cambio en el repositorio de hechos
                    this.repositoryHechos.save(hecho);
                }
                break;
            }
        }
    }

    @Transactional
    public List<CondicionFuente> crearCondicionesDeFuentes(List<Fuente> fuentes){

        List<CondicionFuente> condiciones = new ArrayList<>();
        if(!fuentes.isEmpty()){
            for(Fuente f:fuentes){
                CondicionFuente condicion = new CondicionFuente(f);
                condiciones.add(condicion);
            }
        }
        return condiciones;
    }

    // Nuevo método que elimina de la base los HechoXColeccion que ya no cumplen las condiciones
    private void eliminarHechosNoValidos(List<HechoXColeccion> hechosEnCol, List<InterfaceCondicion> condiciones, List<CondicionFuente> condicionesFuentes, List<Hecho> hechosTotales){
        if(hechosEnCol == null || hechosEnCol.isEmpty()) return;

        // Primero filtrar por fuentes como se hace normalmente
        List<Hecho> hechosFiltradosPorFuente = filtradorDeHechos.devolverHechosDeFuentes(hechosTotales, condicionesFuentes);
        // Luego aplicar las condiciones de pertenencia
        List<Hecho> hechosValidos = filtradorDeHechos.devolverHechosAPartirDe(condiciones, hechosFiltradosPorFuente);
        Set<Long> idsValidos = hechosValidos.stream().map(Hecho::getId_hecho).collect(Collectors.toSet());

        // Iterar sobre una copia para evitar ConcurrentModification
        for(HechoXColeccion hxc : new ArrayList<>(hechosEnCol)){
            if(hxc == null || hxc.getHecho() == null) continue;
            Long idHecho = hxc.getHecho().getId_hecho();
            if(!idsValidos.contains(idHecho)){
                // eliminar de la colección porque ya no cumple
                this.repoHechoxColeccion.delete(hxc);
            }
        }
    }

    @Transactional
    public void ejecutarAlgoritmosDeConsenso() {

        log.info("Iniciando ejecución de algoritmos de consenso");

        List<Coleccion> colecciones = repositoryColecciones.findAll();

        for (Coleccion coleccion : colecciones) {

            List<Fuente> fuentes =
                    repositoryFuente.findFuentesByColeccion(coleccion.getId_coleccion());
            coleccion.setFuentes(fuentes);

            int totalFuentes = fuentes.size();

            List<MencionDeHecho> menciones =
                    repoHechoxColeccion.findMencionesDeHechos(coleccion.getId_coleccion());

            Map<Long, List<MencionDeHecho>> mencionesPorHecho =
                    menciones.stream()
                            .collect(Collectors.groupingBy(MencionDeHecho::hechoId));

            Map<Long, EvidenciaDeHecho> evidenciaPorHecho = new HashMap<>();

            //busco la evidencia del hecho
            for (Map.Entry<Long, List<MencionDeHecho>> entry : mencionesPorHecho.entrySet()) {

                Long hechoId = entry.getKey();
                List<MencionDeHecho> mencionesDelHecho = entry.getValue();

                Set<Long> fuentesQueMencionan =
                        mencionesDelHecho.stream()
                                .map(MencionDeHecho::fuenteId)
                                .collect(Collectors.toSet());

                Set<String> descripciones =
                        mencionesDelHecho.stream()
                                .map(MencionDeHecho::descripcion)
                                .collect(Collectors.toSet());

                boolean hayConflicto = descripciones.size() > 1;

                evidenciaPorHecho.put(
                        hechoId,
                        new EvidenciaDeHecho(hechoId, fuentesQueMencionan, hayConflicto)
                );
            }
            List<HechoXColeccion> hechos = repoHechoxColeccion.findByColeccion(coleccion.getId_coleccion());

            for (HechoXColeccion hxc : hechos) {
                Long hechoId = hxc.getHecho().getId_hecho();

                EvidenciaDeHecho evidencia = evidenciaPorHecho.getOrDefault(
                                hechoId,
                                EvidenciaDeHecho.vacia(hechoId)
                        );

                boolean consensuado =
                        coleccion.getAlgoritmoDeConsenso()
                                .aplicar(evidencia, totalFuentes);

                hxc.setConsensuado(consensuado);
            }
        }
        repositoryColecciones.saveAll(colecciones);
        log.info("Algoritmos de consenso finalizados");
    }
}
