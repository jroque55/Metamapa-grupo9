package utn.ddsi.agregador.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import utn.ddsi.agregador.domain.fuentes.Fuente;
import utn.ddsi.agregador.domain.hecho.*;
import utn.ddsi.agregador.dto.HechoFuenteDinamicaDTO;
import utn.ddsi.agregador.dto.HechoFuenteEstaticaDTO;
import utn.ddsi.agregador.dto.HechoFuenteProxyDTO;
import utn.ddsi.agregador.repository.IRepositoryCategorias;
import utn.ddsi.agregador.repository.IRepositoryFuentes;
import utn.ddsi.agregador.utils.EnumEstadoHecho;
import utn.ddsi.agregador.utils.EnumTipoFuente;
import utn.ddsi.agregador.utils.EnumTipoHecho;
import utn.ddsi.agregador.utils.TipoMedia;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Component
public class HechoAdapter {

    @Value("${fuente.proxy.url}")
    private String proxyUrl;
    @Value("${fuente.dinamica.url}")
    private String dinamicaUrl;

    @Autowired
    private final IRepositoryFuentes repoFuente;
    public HechoAdapter(IRepositoryFuentes repoFuente) {
        this.repoFuente = repoFuente;
    }

    public Hecho adaptarDesdeFuenteDinamica(HechoFuenteDinamicaDTO dto, Fuente fuente) {
        // convierto componentes
        Categoria categoria = new Categoria(dto.getCategoria());

        Ubicacion ubicacion = null;
        if (dto.getUbicacion() != null) {
            ubicacion = new Ubicacion(
                dto.getUbicacion().getLatitud(),
                dto.getUbicacion().getLongitud()
            );
        }

        Hecho hecho = new Hecho(
            dto.getTitulo(),
            dto.getDescripcion(),
            categoria,
            ubicacion,
            dto.getFecha(),
            fuente
        );

        // Determinar tipo de hecho
        if (dto.getTipoDeHecho() != null) {
            hecho.setTipoHecho(EnumTipoHecho.valueOf(dto.getTipoDeHecho()));
        }

        // Adaptar adjuntos
        if (dto.getAdjuntos() != null && !dto.getAdjuntos().isEmpty()) {
            List<Adjunto> adjuntos = new ArrayList<>();
            for (var adjuntoDTO : dto.getAdjuntos()) {
                Adjunto adjunto = new Adjunto();
                adjunto.setUrl(adjuntoDTO.getUrl());
                if (adjuntoDTO.getTipo() != null) {
                    adjunto.setTipo(TipoMedia.valueOf(adjuntoDTO.getTipo()));
                }
                adjuntos.add(adjunto);
            }
            hecho.setAdjuntos(adjuntos);
        }
        hecho.setEstado(EnumEstadoHecho.ALTA);
        hecho.setFechaDeCarga(LocalDateTime.now());

        return hecho;
    }
    public Hecho adaptarDesdeFuenteProxy(HechoFuenteProxyDTO dto, Fuente fuente){
        Categoria categoria = new Categoria(dto.getCategoria());

        Ubicacion ubicacion = new Ubicacion(Float.valueOf( dto.getUbicacionLat()),Float.valueOf(dto.getUbicacionLon())); //Chequear orden

        Hecho hecho = new Hecho(
                dto.getTitulo(),
                dto.getDescripcion(),
                categoria,
                ubicacion,
                LocalDate.parse(dto.getFecha()),
                fuente
        );

        hecho.setEstado(EnumEstadoHecho.ALTA);
        hecho.setTipoHecho(EnumTipoHecho.valueOf(dto.getTipoHecho()));
        hecho.setFechaDeCarga(LocalDateTime.now());
        return hecho;
    }

    public List<Hecho> adaptarHechosDeFuenteProxy(List<HechoFuenteProxyDTO> hechosDTO) {

        Fuente fuente = this.repoFuente.findFirstByTipoFuente(EnumTipoFuente.METAMAPA);
        if (fuente == null) {
            fuente = new Fuente();
            fuente.setTipoFuente(EnumTipoFuente.METAMAPA);
            fuente.setUrl(this.proxyUrl);
            fuente.setNombre("Proxy");
            fuente = this.repoFuente.save(fuente);
        }

        List<Hecho> hechos = new ArrayList<>();
        for (HechoFuenteProxyDTO hechoFuenteProxyDTO : hechosDTO) {
            Hecho h = adaptarDesdeFuenteProxy(hechoFuenteProxyDTO, fuente);
            hechos.add(h);
        }
        return hechos;
    }

    public Hecho adaptarDesdeFuenteEstatica(HechoFuenteEstaticaDTO dto, Fuente fuente) {
        Categoria categoria = new Categoria(dto.getCategoria().getNombre());

        Ubicacion ubicacion = new Ubicacion(dto.getLatitud(), dto.getLongitud());

        Hecho hecho = new Hecho(
                dto.getTitulo(),
                dto.getDescripcion(),
                categoria,
                ubicacion,
                dto.getFecha(),
                fuente
        );
        hecho.setTipoHecho(EnumTipoHecho.TEXTO);
        hecho.setEstado(EnumEstadoHecho.ALTA);
        hecho.setFechaDeCarga(LocalDateTime.now());
        return hecho;
    }

    public List<Hecho> adaptarHechosDeFuenteDinamica(List<HechoFuenteDinamicaDTO> hechosDTO) {
        Fuente fuente = this.repoFuente.findFirstByTipoFuente(EnumTipoFuente.DINAMICA);
        if (fuente == null) {
            fuente = new Fuente();
            fuente.setTipoFuente(EnumTipoFuente.DINAMICA);
            fuente.setUrl(this.dinamicaUrl);
            fuente.setNombre("Dinamica");
            fuente = this.repoFuente.save(fuente);
        }
        List<Hecho> hechos = new ArrayList<>();
        for (HechoFuenteDinamicaDTO hechoFuenteDinamicaDTO : hechosDTO) {
            Hecho h = adaptarDesdeFuenteDinamica(hechoFuenteDinamicaDTO, fuente);
            hechos.add(h);
        }
        return hechos;
    }

    public List<Hecho> adaptarHechosDeFuenteEstatica(String ruta, List<HechoFuenteEstaticaDTO> hechosDTO){
        Fuente fuente = this.repoFuente.findByUrl(ruta);
        if(fuente == null) {
            return new ArrayList<>();
        }
        List<Hecho> hechos = new ArrayList<>();
        for(int i = 0; i < hechosDTO.size(); i++){
            Hecho h = adaptarDesdeFuenteEstatica(hechosDTO.get(i), fuente);
            hechos.add(h);
        }
        return hechos;
    }


}
