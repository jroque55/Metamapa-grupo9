package ar.utn.ba.ddsi.apipublica.services;

import ar.utn.ba.ddsi.apipublica.exception.RecursoNoEncontradoException;
import ar.utn.ba.ddsi.apipublica.models.dtos.ColeccionOutputDTO;
import ar.utn.ba.ddsi.apipublica.models.dtos.HechoFilterDTO;
import ar.utn.ba.ddsi.apipublica.models.dtos.ColeccionFilterDTO;
import ar.utn.ba.ddsi.apipublica.models.dtos.HechoOutputDTO;
import ar.utn.ba.ddsi.apipublica.models.entities.*;
import ar.utn.ba.ddsi.apipublica.models.repository.ColeccionRepository;
import ar.utn.ba.ddsi.apipublica.models.repository.FuenteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class ColeccionService {
    //No tengo el Filtrador de Hechos, asi que delego todo al Repository ESTARÁ BIEN?????
    private final ColeccionRepository coleccionRepository;
    private final FuenteRepository fuenteRepository;

    public ColeccionService(ColeccionRepository coleccionRepository, FuenteRepository fuenteRepository) {
        this.coleccionRepository = coleccionRepository;
        this.fuenteRepository = fuenteRepository;
    }

    public List<HechoOutputDTO> buscarHechosSegun(HechoFilterDTO filter, String modoDeNavegacion, Long coleccionId) {

       log.info("Iniciando búsqueda de hechos con filtro en colección ID: {}", coleccionId);
        if (coleccionId == null) {
            log.warn("Intento de búsqueda de hechos sin ID de colección");
            throw new IllegalArgumentException("El ID de la colección no puede ser nulo");
        }
        if (coleccionRepository.findById(coleccionId).isEmpty()) {
            log.warn("Colección no encontrada con ID: {}", coleccionId);
            throw new IllegalArgumentException("Colección no existe: " + coleccionId);
        }

        if (filter == null) filter = new HechoFilterDTO();

        // Validar y parsear el DTO (mueve la lógica de validación al propio DTO)
        filter.validateAndParse();

        log.info("Normalazando categoria y determinando coordenadas para búsqueda");
        // Normalizar categoria
        String categoriaNombre = null;
        if (filter.getCategoria() != null && !filter.getCategoria().isBlank()) {
            categoriaNombre = filter.getCategoria().trim();
        }

        // Determinar delta si hay coordenadas
        Float delta = null;
        if (filter.getUbicacionLatitudParsed() != null && filter.getUbicacionLongitudParsed() != null) {
            delta = 0.01f; // tolerancia por defecto
        }

        Boolean curado = null; // null significa no filtrar por consensuado
            String textoLibre = null;
            if (filter.getTextoLibre() != null && !filter.getTextoLibre().isBlank()) {
                textoLibre = filter.getTextoLibre().trim();

            }
            String tipoDeFuente = null;
            if (filter.getTipoFuente() != null && !filter.getTipoFuente().isBlank()) {
                tipoDeFuente = filter.getTipoFuente().trim();
            }

            // Evitar NPE: normalizar modoDeNavegacion
            String modo = (modoDeNavegacion == null) ? "" : modoDeNavegacion.trim();

            if (modo.equalsIgnoreCase("CURADA") || modo.equalsIgnoreCase("CURADO")) {
                curado = Boolean.TRUE;
                log.info("Modo de navegación CURADA: buscando solo hechos consensuados en la colección");
                return PasarAHechosDTO(coleccionRepository.buscarEnColeccionSegun(
                        coleccionId,
                        categoriaNombre,
                        filter.getFechaReporteDesdeParsed(),
                        filter.getFechaReporteHastaParsed(),
                        filter.getFechaAcontecimientoDesdeParsed(),
                        filter.getFechaAcontecimientoHastaParsed(),
                        filter.getProvincia(),
                        delta,
                        curado,
                        textoLibre,
                        tipoDeFuente
                ));
            }else {
                log.info("Modo de navegación IRRESTRICTA: buscando todos los hechos en la colección sin filtrar por consensuado");
                return PasarAHechosDTO(coleccionRepository.buscarEnColeccionIrrestricta(coleccionId,
                        categoriaNombre,
                        filter.getFechaReporteDesdeParsed(),
                        filter.getFechaReporteHastaParsed(),
                        filter.getFechaAcontecimientoDesdeParsed(),
                        filter.getFechaAcontecimientoHastaParsed(),
                        filter.getProvincia(),
                        delta,
                        textoLibre,
                        tipoDeFuente));
            }
        }

    public List<ColeccionOutputDTO> buscarColeccionesSegun(ColeccionFilterDTO filter) {

       log.info("Iniciando búsqueda de colecciones con filtro");
        if (filter == null) filter = new ColeccionFilterDTO();


        List<Long> fuenteIds = filter.parseFuenteIdsOrNull();

        String tipo = filter.getTipoAlgoritmo();
        if (tipo != null && tipo.isBlank()) tipo = null;
        String titulo = filter.getTitulo(); if (titulo != null && titulo.isBlank()) titulo = null;
        String descripcion = filter.getDescripcion(); if (descripcion != null && descripcion.isBlank()) descripcion = null;

        // Si se pasó fuente, validar su existencia
        if (fuenteIds != null) {
            log.debug("Validando existencia de fuentes: {}", fuenteIds);
            for (Long fid : fuenteIds) {
                if (fuenteRepository.findById(fid).isEmpty()) {
                    log.warn("Fuente no encontrada con ID: {}", fid);
                    throw new IllegalArgumentException("Fuente no existe: " + fid);
                }
            }
        }

        // Parsear tipo a Enum
        EnumTipoDeAlgoritmo tipoEnum = null;
        if (tipo != null) {
            try {
                tipoEnum = EnumTipoDeAlgoritmo.valueOf(tipo.toUpperCase());
            } catch (IllegalArgumentException iae) {
                log.warn("Tipo de algoritmo inválido: {}", tipo);
                throw new IllegalArgumentException("tipo_algoritmo inválido: " + tipo);
            }
        }
        List<ColeccionOutputDTO> resultado = listaAColeccionOutputDTO(coleccionRepository.buscarColeccionesSegun(titulo, descripcion, tipoEnum, fuenteIds));
        log.debug("Búsqueda de colecciones finalizada. Resultados encontrados: {}", resultado.size());
        return resultado;
    }
    public List<ColeccionOutputDTO> listaAColeccionOutputDTO(List<Coleccion> colecciones){
        List<ColeccionOutputDTO> coleccionesDTO = new ArrayList<>();
        colecciones.forEach(coleccion -> {
            coleccionesDTO.add(new ColeccionOutputDTO(coleccion));
        });
        return coleccionesDTO;
    }
    public ColeccionOutputDTO buscarColeccionPorId(Long id) {
        log.info("Buscando colección por ID: {}", id);

        return coleccionRepository.findById(id)
                .map(ColeccionOutputDTO::new)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Colección no encontrada con ID: " + id));
    }

    public List<HechoOutputDTO> PasarAHechosDTO(List<Hecho> hechos) {
        System.out.println("Convirtiendo "+ hechos.size() +"hechos a DTO");
        List<HechoOutputDTO> resultadoDTO = new ArrayList<>();
        if(hechos==null) return resultadoDTO;
        hechos.forEach(h -> {
            HechoOutputDTO dto = new HechoOutputDTO(h);
            System.out.println("Instancia " + dto);
            resultadoDTO.add(dto);
            System.out.println("Hecho convertido a DTO: " + dto);
        });
        return resultadoDTO;
    }
}
