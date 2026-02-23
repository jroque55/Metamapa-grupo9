package ar.utn.ba.ddsi.apipublica.services;

import ar.utn.ba.ddsi.apipublica.exception.RecursoNoEncontradoException;
import ar.utn.ba.ddsi.apipublica.models.dtos.HechoCreateDTO;
import ar.utn.ba.ddsi.apipublica.models.dtos.HechoFilterDTO;
import ar.utn.ba.ddsi.apipublica.models.dtos.HechoOutputDTO;
import ar.utn.ba.ddsi.apipublica.models.entities.*;
import ar.utn.ba.ddsi.apipublica.models.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class HechoService implements IHechoService {

    private final HechoRepository hechoRepository;
    private final CategoriaRepository categoriaRepository;
    private final FuenteRepository fuenteRepository;
    private final UbicacionRepository ubicacionRepository;
    private final AdjuntoRepository adjuntoRepository;

    public HechoService(HechoRepository hechoRepository,
                        CategoriaRepository categoriaRepository,
                        FuenteRepository fuenteRepository,
                        UbicacionRepository ubicacionRepository,
                        AdjuntoRepository adjuntoRepository) {
        this.hechoRepository = hechoRepository;
        this.categoriaRepository = categoriaRepository;
        this.fuenteRepository = fuenteRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.adjuntoRepository = adjuntoRepository;
    }

    @Override
    public Hecho crearHecho(HechoCreateDTO dto){
        // Lógica de conversión y creación
        log.info("Creando hecho a partir del DTO: {}", dto);
        Hecho hecho = new Hecho();
        hecho.setTitulo(dto.getTitulo());
        hecho.setDescripcion(dto.getDescripcion());

        // Conversión segura de fecha
        if (dto.getFecha() != null && !dto.getFecha().isBlank()) {
            try {
                hecho.setFecha(LocalDate.parse(dto.getFecha()));
            } catch (DateTimeParseException e) {
                // excepción personalizada si la fecha es inválida  VER ESTO DEL FORMATO
                log.warn("Formato de fecha inválido para el hecho: {}", dto.getFecha());
                throw new IllegalArgumentException("Formato de fecha inválido: " + dto.getFecha());
            }
        }

        // Categoria: buscar por nombre, si no existe -> error
        if (dto.getCategoria() != null && !dto.getCategoria().isBlank()) {
            Optional<Categoria> catOpt = categoriaRepository.findByNombreIgnoreCase(dto.getCategoria());
            if (catOpt.isEmpty()) {
                log.warn("Categoria no encontrada: {}", dto.getCategoria());
                throw new IllegalArgumentException("Categoria no existe: " + dto.getCategoria());
            }
            hecho.setCategoria(catOpt.get());
        }
        // Fuente: viene id numerico, intento buscar-> error
        if (dto.getFuente() != null && !dto.getFuente().isBlank()) {
            boolean fuenteSet = false;
            try {
                long idFuente = Long.parseLong(dto.getFuente());
                Optional<Fuente> fOpt = fuenteRepository.findById(idFuente);
                if (fOpt.isPresent()) {
                    hecho.setFuente(fOpt.get());
                    fuenteSet = true;
                }
            } catch (NumberFormatException nfe) {
                // No es un id; intento buscar por nombre  //ES una buena opcion hablar conlos chicos
                Optional<Fuente> fOpt2 = fuenteRepository.findByNombreIgnoreCase(dto.getFuente());
                if (fOpt2.isPresent()) {
                    hecho.setFuente(fOpt2.get());
                    fuenteSet = true;
                }
            }
            if (!fuenteSet) {
                log.warn("Fuente no encontrada: {}", dto.getFuente());
                throw new IllegalArgumentException("Fuente no existe: " + dto.getFuente());
            }
        }
        if (dto.getUbicacionLat() != null && !dto.getUbicacionLat().isBlank()
                && dto.getUbicacionLon() != null && !dto.getUbicacionLon().isBlank()) {
            try {
                float lat = Float.parseFloat(dto.getUbicacionLat());
                float lon = Float.parseFloat(dto.getUbicacionLon());
                // Crear y guardar provincia con un genrador
                Ubicacion u = new Ubicacion(lat, lon,new Provincia("as","eeee"));
                hecho.setUbicacion(ubicacionRepository.save(u));
            } catch (NumberFormatException nfe) {
                log.warn("Latitud o longitud inválida: lat='{}', lon='{}'", dto.getUbicacionLat(), dto.getUbicacionLon());
                throw new IllegalArgumentException("Latitud o longitud inválida");
            }
        }

        // Tipo de hecho: intentar parsear el enum
        if(dto.getTipoHecho()==null){hecho.setTipoHecho(EnumTipoHecho.TEXTO);}
        if (dto.getTipoHecho() != null && !dto.getTipoHecho().isBlank()) {
            try {
                EnumTipoHecho tipo = EnumTipoHecho.valueOf(dto.getTipoHecho());
                hecho.setTipoHecho(tipo);
            } catch (IllegalArgumentException iae) {
                log.warn("Tipo de hecho inválido: {}", dto.getTipoHecho());
                throw new IllegalArgumentException("Tipo de hecho inválido: " + dto.getTipoHecho());
            }
        }
        //PROBAR ESTO
        List<Adjunto> listaAdjuntos = new ArrayList<>();
        if (dto.getAdjuntos() != null) {
            for (String a : dto.getAdjuntos()) {
                if (a == null || a.isBlank()) continue;
                Adjunto adj = new Adjunto();
                adj.setTipo(null);
                adj.setUrl(a);
                listaAdjuntos.add(adjuntoRepository.save(adj));
            }
        }
        hecho.setAdjuntos(listaAdjuntos);

        // Fecha de carga
        log.debug("Asignando fecha de carga actual al hecho");
        hecho.setFechaDeCarga(LocalDateTime.now());

        // Guardar en la base de datos
        //Deberìa mandarselo al Agregador de Hechos
        Hecho hechoGuardado = hechoRepository.save(hecho);
        log.info("Hecho creado con ID: {}", hechoGuardado.getId_hecho());
        return hechoGuardado;
    }

    @Override
    public HechoOutputDTO obtenerHechoPorId(Long id) {

        log.info("Obteniendo hecho por ID: {}", id);
        Hecho hecho = hechoRepository.findById(id).orElse(null);

        if (hecho != null) {
            //podria ser una query del repo?
            if (hecho.getEstado() != EnumEstadoHecho.BAJA) {
                return new HechoOutputDTO(hecho);
            }
            else throw new RecursoNoEncontradoException("hecho rechazado");
        }else {
            throw new RecursoNoEncontradoException("Hecho no encontrado con id: " + id);
        }
    }

    @Override
    public Page<HechoOutputDTO> buscarConFiltro(HechoFilterDTO filter, int page, int size) {

        log.debug("Empezando a buscar con filtros en la pagina {} con tamaño {}", page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaDeCarga").descending());
        Page<Hecho> paginaResultados;

        if (filter == null) {

            log.debug("No se recibieron filtros, listando hechos NO rechazados");

            //List<Hecho> all = hechoRepository.findAll().stream().filter(hecho-> hecho.getEstado()!= EnumEstadoHecho.RECHAZADA).toList();
            paginaResultados = hechoRepository.traerHechosNORechazados(pageable);
        } else {
            filter.validateAndParse();

            String categoriaNombre = (filter.getCategoria() != null && !filter.getCategoria().isBlank())
                    ? filter.getCategoria().trim() : null;

            String textoLibre = (filter.getTextoLibre() != null && !filter.getTextoLibre().isBlank())
                    ? filter.getTextoLibre().trim() : null;

            Float delta = (filter.getUbicacionLatitudParsed() != null && filter.getUbicacionLongitudParsed() != null)
                    ? 0.01f : null;

            log.debug("""
                    Filtros aplicados:
                    - categoria: {}
                    - fechaReporteDesde: {}
                    - fechaReporteHasta: {}
                    - fechaAcontecimientoDesde: {}
                    - fechaAcontecimientoHasta: {}
                    - provincia: {}
                    - textoLibre: {}
                    - tipoFuente: {}
                    - latitud: {}
                    - longitud: {}
                    """,
                    categoriaNombre,
                    filter.getFechaReporteDesdeParsed(),
                    filter.getFechaReporteHastaParsed(),
                    filter.getFechaAcontecimientoDesdeParsed(),
                    filter.getFechaAcontecimientoHastaParsed(),
                    filter.getProvincia(),
                    textoLibre,
                    filter.getTipoFuente(),
                    filter.getUbicacionLatitudParsed(),
                    filter.getUbicacionLongitudParsed()
            );


            paginaResultados = hechoRepository.buscarHechosSegun(
                    categoriaNombre,
                    filter.getFechaReporteDesdeParsed(),
                    filter.getFechaReporteHastaParsed(),
                    filter.getFechaAcontecimientoDesdeParsed(),
                    filter.getFechaAcontecimientoHastaParsed(),
                    filter.getProvincia(),
                    delta,
                    textoLibre,
                    filter.getTipoFuente(),
                    pageable // Pasamos el pageable al repo
            );

            log.debug("Resultados obtenidos: {} en página | {} totales",
                    paginaResultados.getNumberOfElements(),
                    paginaResultados.getTotalElements());

        }
        /*
        // Validar y parsear usando el DTO
        filter.validateAndParse();

        String categoriaNombre = null;
        if(filter.getCategoria() != null && !filter.getCategoria().isBlank()) {
            categoriaNombre = filter.getCategoria().trim();
        }

        String textoLibre = null;
        if (filter.getTextoLibre() != null && !filter.getTextoLibre().isBlank()) {
            textoLibre = filter.getTextoLibre().trim();
        }

        // Determinar delta para proximidad en grados (por defecto 0.01 ~ 1km)
        Float delta = null;
        if (filter.getUbicacionLatitudParsed() != null && filter.getUbicacionLongitudParsed() != null) {
            delta = 0.01f; // valor configurable si se desea
        }

        // Usar campos parseados
        List<Hecho> resultados = hechoRepository.buscarHechosSegun(
                categoriaNombre,
                filter.getFechaReporteDesdeParsed(),
                filter.getFechaReporteHastaParsed(),
                filter.getFechaAcontecimientoDesdeParsed(),
                filter.getFechaAcontecimientoHastaParsed(),
                filter.getUbicacionLatitudParsed(),
                filter.getUbicacionLongitudParsed(),
                delta,
                textoLibre
        );
        System.out.println("Resultados encontrados: " + (resultados == null ? 0 : resultados.size()));
        List<HechoOutputDTO> resultadosDTO= PasarAHechosDTO(resultados);
        System.out.println("Resultados pasados: " + resultadosDTO.size());

         */

        if (paginaResultados.isEmpty()) {
            log.info("La búsqueda no arrojó resultados");
        }
        return paginaResultados.map(HechoOutputDTO::new);
    }

    public List<HechoOutputDTO> PasarAHechosDTO(List<Hecho> hechos) {

        log.debug("Convirtiendo {} hechos a DTO", hechos.size());
        List<HechoOutputDTO> resultadoDTO = new ArrayList<>();
        if(hechos==null) return resultadoDTO;
        hechos.forEach(h -> {
            HechoOutputDTO dto = new HechoOutputDTO(h);

            resultadoDTO.add(dto);
           log.debug("Convertido hecho ID {} a DTO", h.getId_hecho());
        });
        return resultadoDTO;
     }
}
