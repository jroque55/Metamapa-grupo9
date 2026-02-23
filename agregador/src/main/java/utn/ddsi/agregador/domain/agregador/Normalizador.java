package utn.ddsi.agregador.domain.agregador;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.bcel.classfile.EnumElementValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utn.ddsi.agregador.domain.hecho.Adjunto;
import utn.ddsi.agregador.domain.hecho.Categoria;
import utn.ddsi.agregador.domain.hecho.Hecho;
import utn.ddsi.agregador.domain.hecho.Provincia;
import utn.ddsi.agregador.domain.hecho.Ubicacion;
import utn.ddsi.agregador.repository.IRepositoryCategorias;
import utn.ddsi.agregador.repository.IRepositoryProvincias;
import utn.ddsi.agregador.repository.IRepositoryUbicacion;
import utn.ddsi.agregador.utils.EnumTipoFuente;
@Slf4j
@Component
public class Normalizador {

    private static final Pattern MULTIPLES_ESPACIOS = Pattern.compile("\\s+");
    private final Clock clock;
    private final Map<String, String> categoriasCanonicas;
    private static final double EPSILON = 1e-9;
    private final List<ProvinciaPoligono> provinciasPorCoordenada;
    private final Map<String, Provincia> provinciaCache = new HashMap<>();
    private final IRepositoryCategorias repoCategoria;
    private final IRepositoryProvincias repoProvincia;
    private final IRepositoryUbicacion repoUbicacion;

    @Autowired
    public Normalizador(IRepositoryCategorias repoCategoria, IRepositoryProvincias repoProvincia,IRepositoryUbicacion repoUbicacion) {
        this(Clock.systemDefaultZone(), repoCategoria, repoProvincia, repoUbicacion);
    }
    
    //esto es para los tests
    public Normalizador(Clock clock, IRepositoryCategorias repoCategoria, IRepositoryProvincias repoProvincia,IRepositoryUbicacion repoUbicacion) {
        this.clock = clock;
        this.repoCategoria = repoCategoria;
        this.categoriasCanonicas = inicializarCategoriasCanonicas();
        this.provinciasPorCoordenada = inicializarProvincias();
        this.repoProvincia = repoProvincia;
        this.repoUbicacion = repoUbicacion;
    }

    public List<Hecho> normalizar(List<Hecho> hechos) {

        log.debug("Iniciando normalización de {} hechos", (hechos != null ? hechos.size() : 0));

        List<Categoria> categoriasExistentes = repoCategoria.findAll();
        List<Provincia> provinciasExistentes = repoProvincia.findAll();

        if (hechos == null || hechos.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, Hecho> hechosDepurados = new LinkedHashMap<>();

        for (Hecho hecho : hechos) {
            if (hecho == null) {
                continue;
            }

            limpiarCamposTexto(hecho);
            normalizarFechas(hecho);
            normalizarCategoria(hecho, categoriasExistentes);
            normalizarUbicacion(hecho, provinciasExistentes);
/*
            if(hecho.getUbicacion() == null){
                continue;
            }
*/
            String identificador = construirIdentificador(hecho);
            Hecho existente = hechosDepurados.get(identificador);

            if (existente == null) {
                hechosDepurados.put(identificador, hecho);
            } else {
                //TODO: Implementar con base de datos
                combinarHechos(existente, hecho);
            }
        }
        log.info("Normalización finalizada: {} hechos procesados y depurados", hechosDepurados.size());
        return new ArrayList<>(hechosDepurados.values());
    }

    private void limpiarCamposTexto(Hecho hecho) {
        hecho.setTitulo(normalizarTexto(hecho.getTitulo()));
        hecho.setDescripcion(normalizarTexto(hecho.getDescripcion()));
    }

    private void normalizarFechas(Hecho hecho) {
        LocalDate hoy = LocalDate.now(clock);
        LocalDateTime hoyConHora = LocalDateTime.now(clock);

        LocalDate fechaAcontecimiento = hecho.getFecha();
        LocalDateTime fechaCarga = hecho.getFechaDeCarga();

        if (fechaAcontecimiento == null) {
            fechaAcontecimiento = Optional.ofNullable(fechaCarga.toLocalDate()).orElse(hoy);
        }
        if (fechaAcontecimiento.isAfter(hoy)) {
            fechaAcontecimiento = hoy;
        }

        hecho.setFecha(fechaAcontecimiento);

        if (fechaCarga == null) {
            fechaCarga = hoyConHora;
        }
        if (fechaCarga.isBefore(fechaAcontecimiento.atStartOfDay())) {
            fechaCarga = fechaAcontecimiento.atStartOfDay();
        }
        hecho.setFechaDeCarga(fechaCarga);
    }

    private void normalizarCategoria(Hecho hecho, List<Categoria> existentes) {

        Categoria categoria = hecho.getCategoria();
        if (categoria == null) {
            return;
        }

        String nombre = normalizarTexto(categoria.getNombre());
        if (nombre == null) {
            return;
        }

        String nombreClave = removerAcentos(nombre.toLowerCase(Locale.ROOT));
        String canonico = categoriasCanonicas.get(nombreClave);

        if (canonico == null) {
            canonico = aTitulo(nombre);
        }

        String finalCanonico = canonico;

        Categoria cate = existentes.stream().filter( c -> c.getNombre().equalsIgnoreCase(finalCanonico)).findFirst().orElse(null);

        if(cate != null){
            //categoria.setNombre(canonico);
            log.debug("Categoría '{}' normalizada y encontrada en BD", cate.getNombre());
            hecho.setCategoria(cate);

        } else {
            Categoria nueva = new Categoria();
            nueva.setNombre(canonico);
            existentes.add(nueva);
            nueva = repoCategoria.save(nueva);
            log.info("Nueva categoría creada: '{}'", nueva.getNombre());
            if(nueva.getId_categoria() == null) {
                nueva = repoCategoria.findByNombre(nueva.getNombre());  // ya usa LIMIT 1 en la query
            }
            hecho.setCategoria(nueva);

        }
        /*
        categoria.setNombre(canonico);
        Categoria found = this.repoCategoria.findByNombre(categoria.getNombre());
        if(found == null){
            found = new Categoria();
            found.setNombre(categoria.getNombre());
            this.repoCategoria.save(found);
        }
        hecho.setCategoria(found);
        */
    }

    /*
    private void normalizarUbicacion(Hecho hecho, List<Provincia> existentes) {

        Ubicacion ubicacion = hecho.getUbicacion();
        if (ubicacion == null) {
            return;
        }

        float latitud = ubicacion.getLatitud();
        float longitud = ubicacion.getLongitud();

        if (!coordenadasValidas(latitud, longitud)) {
            hecho.setUbicacion(null);
            return;
        }

        ubicacion.setLatitud(redondear(latitud));
        ubicacion.setLongitud(redondear(longitud));

        if (ubicacion.getProvincia() == null) {
            //SE IDENTIFICA NADA MAS SI ESTA EN ARGENTINA
            String nombre = identificarProvincia(ubicacion.getLatitud(), ubicacion.getLongitud());
            // NO ES UNA PROVINCIA DE ARGENTINA
            if (nombre == null) {
                Provincia exterior = existentes.stream()
                        .filter(p -> "EXTERIOR".equalsIgnoreCase(p.getNombre())) // <--- INVERTIR EL ORDEN
                        .findFirst()
                        .orElse(null);                //si no se creo la instancia de una provincia exterior
                if (exterior == null) {
                    Provincia nueva = new Provincia();
                    nueva.setNombre("EXTERIOR");
                    nueva.setPais("EXTERIOR");
                    existentes.add(nueva);
                    nueva = repoProvincia.save(nueva);
                }

            } else {
                // SI NO ES NULL, ME FIJO SI ESTA EN LAS PROVINCIAS EXISTENTES
                String finalNombre = nombre; // Variable final para lambda
                Provincia provincia = existentes.stream()
                        .filter(p -> p.getNombre() != null && p.getNombre().equalsIgnoreCase(finalNombre)) // Validar != null
                        .findFirst()
                        .orElse(null);            // LA PROVINCIA ESTA INSTANCIADA EN NUESTRA BD
                if (provincia != null) {
                    ubicacion.setProvincia(provincia);
                } else {
                    // LA PROVINCIA NO ESTA INSTANCIADA EN NUESTRA BD
                    Provincia nueva = new Provincia();
                    nueva.setNombre(finalNombre);
                    nueva.setPais("Argentina");
                    nueva = repoProvincia.save(nueva);
                    ubicacion.setProvincia(nueva);
                    existentes.add(nueva);
                }
            }
            Ubicacion nuevaUbi = repoUbicacion.save(ubicacion);
            hecho.setUbicacion(nuevaUbi);
        }

    }*/

    private void normalizarUbicacion(Hecho hecho, List<Provincia> existentes) {
        Ubicacion ubicacion = hecho.getUbicacion();
        if (ubicacion == null) return;

        float latitud = ubicacion.getLatitud();
        float longitud = ubicacion.getLongitud();

        if (!coordenadasValidas(latitud, longitud)) {
            hecho.setUbicacion(null);
            return;
        }

        ubicacion.setLatitud(redondear(latitud));
        ubicacion.setLongitud(redondear(longitud));

        // Solo procesamos si no tiene provincia asignada
        if (ubicacion.getProvincia() == null) {

            // 1. Identificamos el nombre (será el nombre real o NULL si es exterior)
            String nombreDetectado = identificarProvincia(ubicacion.getLatitud(), ubicacion.getLongitud());
            String nombreFinal = (nombreDetectado != null) ? nombreDetectado : "EXTERIOR";
            String paisFinal = (nombreDetectado != null) ? "Argentina" : "EXTERIOR";

            // 2. Buscamos si ya existe en la lista (Memoria/BD)
            Provincia provincia = existentes.stream()
                    .filter(p -> p.getNombre() != null && p.getNombre().equalsIgnoreCase(nombreFinal))
                    .findFirst()
                    .orElse(null);

            // 3. Si no existe, la creamos y guardamos
            if (provincia == null) {
                provincia = new Provincia();
                provincia.setNombre(nombreFinal);
                provincia.setPais(paisFinal);

                // Guardamos para tener ID
                provincia = repoProvincia.save(provincia);
                log.info("Nueva provincia creada: '{}'", nombreFinal);
                // Agregamos a la lista local para no re-crearla en el siguiente loop
                existentes.add(provincia);
            }

            // 4. 🔥 ASIGNACIÓN FINAL (Esto es lo que te faltaba en el caso EXTERIOR)
            ubicacion.setProvincia(provincia);

            // 5. Guardamos la ubicación (Ahora con provincia asignada)
            // Nota: Si Hecho tiene Cascade.PERSIST sobre Ubicacion, esta línea podría ser redundante,
            // pero no hace daño si quieres asegurar el ID ahora.
            repoUbicacion.save(ubicacion);
        }
    }


    private void combinarHechos(Hecho base, Hecho candidato) {

        log.debug("Combinando hechos: '{}' con '{}'", base.getTitulo(), candidato.getTitulo());


        if (candidato.getDescripcion() != null && (base.getDescripcion() == null
                || candidato.getDescripcion().length() > base.getDescripcion().length())) {
            base.setDescripcion(candidato.getDescripcion());
        }

        if (base.getCategoria() == null && candidato.getCategoria() != null) {
            base.setCategoria(candidato.getCategoria());
        }

        if (base.getUbicacion() == null && candidato.getUbicacion() != null) {
            base.setUbicacion(candidato.getUbicacion());
        } else if (base.getUbicacion() != null && candidato.getUbicacion() != null
                && base.getUbicacion().getProvincia() == null) {
            base.getUbicacion().setProvincia(candidato.getUbicacion().getProvincia());
        }

        if (candidato.getFecha() != null && candidato.getFecha().isBefore(base.getFecha())) {
            base.setFecha(candidato.getFecha());
        }

        if (candidato.getFechaDeCarga() != null && candidato.getFechaDeCarga().isBefore(base.getFechaDeCarga())) {
            base.setFechaDeCarga(candidato.getFechaDeCarga());
        }

        if (deberiaReemplazarFuente(base, candidato)) {
            base.setFuente(candidato.getFuente());
        }

        if (base.getAdjuntos() != null || candidato.getAdjuntos() != null) {
            base.setAdjuntos(unificarAdjuntos(base.getAdjuntos(), candidato.getAdjuntos()));
        }

        if (base.getTipoHecho() == null) {
            base.setTipoHecho(candidato.getTipoHecho());
        }

        if (base.getEtiqueta() == null) {
            base.setEtiqueta(candidato.getEtiqueta());
        }
    }

    private boolean deberiaReemplazarFuente(Hecho base, Hecho candidato) {
        EnumTipoFuente tipoBase = obtenerTipo(base);
        EnumTipoFuente tipoCandidato = obtenerTipo(candidato);

        return prioridad(tipoCandidato) > prioridad(tipoBase);
    }

    private EnumTipoFuente obtenerTipo(Hecho hecho) {
        return hecho.getFuente() == null ? null : hecho.getFuente().getTipoFuente();
    }

    private int prioridad(EnumTipoFuente tipo) {
        if (tipo == null) {
            return -1;
        }
        return switch (tipo) {
            case ESTATICA -> 4;
            case METAMAPA -> 3;
            case DEMO -> 2;
            case DINAMICA -> 1;
        };
    }

    private List<Adjunto> unificarAdjuntos(List<Adjunto> originales, List<Adjunto> nuevos) {
        Map<String, Adjunto> adjuntosPorUrl = new LinkedHashMap<>();

        if (originales != null) {
            for (Adjunto adjunto : originales) {
                if (adjunto != null && adjunto.getUrl() != null) {
                    adjuntosPorUrl.putIfAbsent(adjunto.getUrl(), adjunto);
                }
            }
        }

        if (nuevos != null) {
            for (Adjunto adjunto : nuevos) {
                if (adjunto != null && adjunto.getUrl() != null) {
                    adjuntosPorUrl.putIfAbsent(adjunto.getUrl(), adjunto);
                }
            }
        }

        return new ArrayList<>(adjuntosPorUrl.values());
    }

    private String identificarProvincia(Float latitud, Float longitud) {
        for (ProvinciaPoligono provincia : provinciasPorCoordenada) {
            if (provincia.contiene(latitud, longitud)) {
                return provincia.nombre();
            }
        }
        return null;
    }

    private Provincia buscarOCrearProvincia(String nombre) {
        Provincia existente = this.repoProvincia.findFirstByNombre(nombre);

        if (existente != null) {
            return existente;
        }

        Provincia nueva = new Provincia();
        nueva.setNombre(nombre);
        nueva.setPais("Argentina");

        return this.repoProvincia.save(nueva);
    }


    private Provincia obtenerProvinciaExterior() {
        return provinciaCache.computeIfAbsent("EXTERIOR", key -> {
            Provincia existente = this.repoProvincia.findFirstByNombre("EXTERIOR");

            if (existente != null) {
                return existente;
            }

            Provincia provincia = new Provincia();
            provincia.setNombre("EXTERIOR");
            provincia.setPais("EXTERIOR");

            return this.repoProvincia.save(provincia);
        });
    }

    private boolean coordenadasValidas(float latitud, float longitud) {
        return latitud >= -90 && latitud <= 90 && longitud >= -180 && longitud <= 180;
    }

    private float redondear(float valor) {
        return (float) (Math.round(valor * 10000.0) / 10000.0);
    }

    private String construirIdentificador(Hecho hecho) {
        String baseTitulo = Optional.ofNullable(hecho.getTitulo())
                .orElse(Optional.ofNullable(hecho.getDescripcion()).orElse(""));
        String tituloNormalizado = removerAcentos(baseTitulo.toLowerCase(Locale.ROOT));
        Ubicacion ubicacion = hecho.getUbicacion();
        if (ubicacion != null) {
            String ubicacionClave = String.format(Locale.ROOT, "%.2f|%.2f", ubicacion.getLatitud(), ubicacion.getLongitud());
            return tituloNormalizado + "|" + ubicacionClave;
        }

        LocalDate fecha = hecho.getFecha();
        String fechaClave = fecha == null ? "" : fecha.toString();
        return tituloNormalizado + "|" + fechaClave;
    }

    private Map<String, String> inicializarCategoriasCanonicas() {
        Map<String, String> mapa = new HashMap<>();
        mapa.put("incendio forestal", "Incendio forestal");
        mapa.put("fuego forestal", "Incendio forestal");
        mapa.put("forest fire", "Incendio forestal");
        mapa.put("wildfire", "Incendio forestal");
        mapa.put("inundacion", "Inundacion");
        mapa.put("flood", "Inundacion");
        mapa.put("contaminacion", "Contaminacion");
        mapa.put("pollution", "Contaminacion");
        mapa.put("derrame quimico", "Derrame quimico");
        mapa.put("chemical spill", "Derrame quimico");
        mapa.put("desaparicion", "Desaparicion");
        mapa.put("missing person", "Desaparicion");
        mapa.put("deforestacion", "Deforestacion");
        mapa.put("desforestacion", "Deforestacion");
        return mapa;
    }

    private List<ProvinciaPoligono> inicializarProvincias() {
        List<ProvinciaPoligono> provincias = cargarProvinciasDesdeGeometrias();
        return provincias.isEmpty() ? inicializarProvinciasFallback() : provincias;
    }

    private List<ProvinciaPoligono> cargarProvinciasDesdeGeometrias() {
        try (InputStream inputStream = Normalizador.class.getClassLoader()
                .getResourceAsStream("Provincias (2010).csv")) {
            if (inputStream == null) {
                return List.of();
            }

            String contenido = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            List<List<String>> filas = parsearCsv(contenido);
            if (filas.isEmpty()) {
                return List.of();
            }

            ObjectMapper objectMapper = new ObjectMapper();
            List<ProvinciaPoligono> provincias = new ArrayList<>();
            boolean encabezadoProcesado = false;

            for (List<String> fila : filas) {
                if (!encabezadoProcesado) {
                    encabezadoProcesado = true;
                    continue;
                }
                if (fila.size() < 6) {
                    continue;
                }

                String nombreOriginal = fila.get(1);
                String geoJson = fila.get(5);
                String nombre = normalizarNombreProvincia(nombreOriginal);

                if (nombre == null || geoJson == null || geoJson.isBlank()) {
                    continue;
                }

                ProvinciaPoligono provincia = construirProvinciaDesdeGeoJson(nombre, geoJson, objectMapper);
                if (provincia != null) {
                    provincias.add(provincia);
                }
            }

            return provincias;
        } catch (Exception e) {
            return List.of();
        }
    }

    private ProvinciaPoligono construirProvinciaDesdeGeoJson(String nombre, String geoJson, ObjectMapper mapper) {
        try {
            JsonNode nodo = mapper.readTree(geoJson);
            String tipo = nodo.path("type").asText();
            JsonNode coordenadas = nodo.path("coordinates");
            List<Poligono> poligonos = new ArrayList<>();

            if ("Polygon".equalsIgnoreCase(tipo)) {
                Poligono poligono = parsearPoligono(coordenadas);
                if (poligono != null) {
                    poligonos.add(poligono);
                }
            } else if ("MultiPolygon".equalsIgnoreCase(tipo)) {
                for (JsonNode poligonoNode : coordenadas) {
                    Poligono poligono = parsearPoligono(poligonoNode);
                    if (poligono != null) {
                        poligonos.add(poligono);
                    }
                }
            }

            if (poligonos.isEmpty()) {
                return null;
            }

            Bounds limites = combinarLimites(poligonos);
            return new ProvinciaPoligono(nombre, poligonos, limites);
        } catch (Exception e) {
            return null;
        }
    }

    private Poligono parsearPoligono(JsonNode poligonoNode) {
        if (poligonoNode == null || !poligonoNode.isArray() || poligonoNode.isEmpty()) {
            return null;
        }

        List<Punto> exterior = parsearAnillo(poligonoNode.get(0));
        if (exterior.size() < 3) {
            return null;
        }

        List<List<Punto>> huecos = new ArrayList<>();
        for (int i = 1; i < poligonoNode.size(); i++) {
            List<Punto> hueco = parsearAnillo(poligonoNode.get(i));
            if (hueco.size() >= 3) {
                huecos.add(hueco);
            }
        }

        return crearPoligono(exterior, huecos);
    }

    private List<Punto> parsearAnillo(JsonNode anilloNode) {
        List<Punto> puntos = new ArrayList<>();
        if (anilloNode != null && anilloNode.isArray()) {
            for (JsonNode coordenada : anilloNode) {
                if (coordenada.isArray() && coordenada.size() >= 2) {
                    double lon = coordenada.get(0).asDouble();
                    double lat = coordenada.get(1).asDouble();
                    puntos.add(new Punto(lon, lat));
                }
            }
        }
        return normalizarAnillo(puntos);
    }

    private List<Punto> normalizarAnillo(List<Punto> puntos) {
        if (puntos.isEmpty()) {
            return puntos;
        }

        List<Punto> normalizados = new ArrayList<>();
        Punto previo = null;
        for (Punto punto : puntos) {
            if (previo == null || !puntosIguales(previo, punto)) {
                normalizados.add(punto);
                previo = punto;
            }
        }

        if (normalizados.size() > 1
                && puntosIguales(normalizados.get(0), normalizados.get(normalizados.size() - 1))) {
            normalizados.remove(normalizados.size() - 1);
        }

        return normalizados;
    }

    private Poligono crearPoligono(List<Punto> exterior, List<List<Punto>> huecos) {
        Bounds limites = Bounds.desdeAnillo(exterior);
        return new Poligono(exterior, huecos, limites);
    }

    private Bounds combinarLimites(List<Poligono> poligonos) {
        double minLat = Double.POSITIVE_INFINITY;
        double maxLat = Double.NEGATIVE_INFINITY;
        double minLon = Double.POSITIVE_INFINITY;
        double maxLon = Double.NEGATIVE_INFINITY;

        for (Poligono poligono : poligonos) {
            Bounds limite = poligono.bounds();
            minLat = Math.min(minLat, limite.minLat());
            maxLat = Math.max(maxLat, limite.maxLat());
            minLon = Math.min(minLon, limite.minLon());
            maxLon = Math.max(maxLon, limite.maxLon());
        }

        return new Bounds(minLat, maxLat, minLon, maxLon);
    }

    private List<ProvinciaPoligono> inicializarProvinciasFallback() {
        List<ProvinciaPoligono> provincias = new ArrayList<>();
        provincias.add(desdeBoundingBox("Ciudad Autonoma de Buenos Aires", -34.74f, -34.50f, -58.53f, -58.35f));
        provincias.add(desdeBoundingBox("Buenos Aires", -41.0f, -33.0f, -64.5f, -56.0f));
        provincias.add(desdeBoundingBox("Cordoba", -35.5f, -29.0f, -66.6f, -61.0f));
        provincias.add(desdeBoundingBox("Santa Fe", -33.8f, -28.5f, -62.5f, -58.0f));
        provincias.add(desdeBoundingBox("Mendoza", -37.5f, -31.5f, -70.8f, -66.5f));
        provincias.add(desdeBoundingBox("Neuquen", -41.5f, -36.0f, -72.5f, -68.5f));
        provincias.add(desdeBoundingBox("Rio Negro", -42.5f, -37.0f, -71.5f, -63.0f));
        provincias.add(desdeBoundingBox("Chubut", -46.5f, -42.0f, -71.5f, -66.0f));
        provincias.add(desdeBoundingBox("Santa Cruz", -52.5f, -46.0f, -73.0f, -66.0f));
        provincias.add(desdeBoundingBox("Tierra del Fuego", -55.2f, -52.5f, -68.5f, -63.5f));
        provincias.add(desdeBoundingBox("Salta", -26.5f, -22.0f, -68.5f, -63.5f));
        provincias.add(desdeBoundingBox("Jujuy", -24.9f, -21.8f, -66.9f, -64.0f));
        provincias.add(desdeBoundingBox("Tucuman", -27.5f, -26.0f, -66.5f, -64.5f));
        provincias.add(desdeBoundingBox("Catamarca", -29.8f, -24.0f, -69.5f, -64.0f));
        provincias.add(desdeBoundingBox("La Rioja", -31.8f, -27.0f, -68.5f, -64.0f));
        provincias.add(desdeBoundingBox("San Juan", -32.5f, -28.0f, -69.8f, -66.0f));
        provincias.add(desdeBoundingBox("San Luis", -34.5f, -31.0f, -67.5f, -64.0f));
        provincias.add(desdeBoundingBox("La Pampa", -39.5f, -35.0f, -68.0f, -63.5f));
        provincias.add(desdeBoundingBox("Entre Rios", -33.5f, -27.8f, -60.8f, -57.0f));
        provincias.add(desdeBoundingBox("Corrientes", -30.5f, -27.0f, -59.9f, -55.8f));
        provincias.add(desdeBoundingBox("Misiones", -28.3f, -25.3f, -56.1f, -53.9f));
        provincias.add(desdeBoundingBox("Chaco", -28.5f, -24.0f, -63.0f, -58.0f));
        provincias.add(desdeBoundingBox("Formosa", -26.5f, -22.0f, -61.5f, -57.5f));
        provincias.add(desdeBoundingBox("Santiago del Estero", -30.5f, -25.0f, -65.9f, -61.0f));
        return provincias;
    }

    private ProvinciaPoligono desdeBoundingBox(String nombre, float latMin, float latMax, float lonMin, float lonMax) {
        List<Punto> exterior = new ArrayList<>();
        exterior.add(new Punto(lonMin, latMin));
        exterior.add(new Punto(lonMax, latMin));
        exterior.add(new Punto(lonMax, latMax));
        exterior.add(new Punto(lonMin, latMax));
        exterior.add(new Punto(lonMin, latMin));

        Poligono poligono = crearPoligono(normalizarAnillo(exterior), List.of());
        return new ProvinciaPoligono(nombre, List.of(poligono), poligono.bounds());
    }

    private List<List<String>> parsearCsv(String contenido) {
        List<List<String>> filas = new ArrayList<>();
        List<String> filaActual = new ArrayList<>();
        StringBuilder campoActual = new StringBuilder();
        boolean enComillas = false;

        for (int i = 0; i < contenido.length(); i++) {
            char caracter = contenido.charAt(i);

            if (caracter == '"') {
                if (enComillas && i + 1 < contenido.length() && contenido.charAt(i + 1) == '"') {
                    campoActual.append('"');
                    i++;
                } else {
                    enComillas = !enComillas;
                }
            } else if (caracter == ',' && !enComillas) {
                filaActual.add(campoActual.toString());
                campoActual.setLength(0);
            } else if ((caracter == '\n' || caracter == '\r') && !enComillas) {
                if (caracter == '\r' && i + 1 < contenido.length() && contenido.charAt(i + 1) == '\n') {
                    i++;
                }
                filaActual.add(campoActual.toString());
                campoActual.setLength(0);
                if (!filaActual.isEmpty() && filaActual.stream().anyMatch(c -> !c.isEmpty())) {
                    filas.add(filaActual);
                }
                filaActual = new ArrayList<>();
            } else {
                campoActual.append(caracter);
            }
        }

        if (campoActual.length() > 0 || !filaActual.isEmpty()) {
            filaActual.add(campoActual.toString());
            if (!filaActual.isEmpty() && filaActual.stream().anyMatch(c -> !c.isEmpty())) {
                filas.add(filaActual);
            }
        }

        return filas;
    }

    private String normalizarNombreProvincia(String nombreOriginal) {
        if (nombreOriginal == null) {
            return null;
        }
        String sinAcentos = removerAcentos(nombreOriginal).trim();
        if (sinAcentos.isEmpty()) {
            return null;
        }

        String compacto = sinAcentos.replaceAll("\\s+", " ");
        String titulo = aTitulo(compacto);
        return restaurarPreposiciones(titulo);
    }

    private String restaurarPreposiciones(String nombre) {
        return nombre
                .replace(" De ", " de ")
                .replace(" Del ", " del ")
                .replace(" La ", " la ")
                .replace(" Las ", " las ")
                .replace(" Los ", " los ")
                .replace(" San ", " San ")
                .replace(" Santa ", " Santa ");
    }

    private String normalizarTexto(String texto) {
        if (texto == null) {
            return null;
        }
        String limpio = MULTIPLES_ESPACIOS.matcher(texto).replaceAll(" ").trim();
        return limpio.isEmpty() ? null : limpio;
    }

    private String removerAcentos(String texto) {
        if (texto == null) {
            return null;
        }
        String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return normalizado.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    private String aTitulo(String texto) {
        String limpio = texto.toLowerCase(Locale.ROOT);
        String[] partes = limpio.split(" ");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < partes.length; i++) {
            String parte = partes[i];
            if (parte.isEmpty()) {
                continue;
            }
            builder.append(Character.toUpperCase(parte.charAt(0)));
            if (parte.length() > 1) {
                builder.append(parte.substring(1));
            }
            if (i < partes.length - 1) {
                builder.append(' ');
            }
        }

        return builder.toString();
    }

    private record ProvinciaPoligono(String nombre, List<Poligono> poligonos, Bounds bounds) {
        boolean contiene(float latitud, float longitud) {
            if (!bounds.contains(latitud, longitud)) {
                return false;
            }
            for (Poligono poligono : poligonos) {
                if (poligono.contiene(latitud, longitud)) {
                    return true;
                }
            }
            return false;
        }
    }

    private record Poligono(List<Punto> exterior, List<List<Punto>> huecos, Bounds bounds) {
        boolean contiene(double latitud, double longitud) {
            if (!bounds.contains(latitud, longitud)) {
                return false;
            }
            if (!puntoEnAnillo(exterior, latitud, longitud)) {
                return false;
            }
            for (List<Punto> hueco : huecos) {
                if (puntoEnAnillo(hueco, latitud, longitud)) {
                    return false;
                }
            }
            return true;
        }
    }

    private record Punto(double lon, double lat) {
    }

    private record Bounds(double minLat, double maxLat, double minLon, double maxLon) {
        boolean contains(double lat, double lon) {
            return lat >= minLat && lat <= maxLat && lon >= minLon && lon <= maxLon;
        }

        static Bounds desdeAnillo(List<Punto> anillo) {
            double minLat = Double.POSITIVE_INFINITY;
            double maxLat = Double.NEGATIVE_INFINITY;
            double minLon = Double.POSITIVE_INFINITY;
            double maxLon = Double.NEGATIVE_INFINITY;

            for (Punto punto : anillo) {
                minLat = Math.min(minLat, punto.lat());
                maxLat = Math.max(maxLat, punto.lat());
                minLon = Math.min(minLon, punto.lon());
                maxLon = Math.max(maxLon, punto.lon());
            }

            return new Bounds(minLat, maxLat, minLon, maxLon);
        }
    }

    private static boolean puntoEnAnillo(List<Punto> anillo, double latitud, double longitud) {
        boolean dentro = false;
        int tam = anillo.size();
        for (int i = 0, j = tam - 1; i < tam; j = i++) {
            Punto puntoI = anillo.get(i);
            Punto puntoJ = anillo.get(j);

            if (puntoSobreSegmento(latitud, longitud, puntoI, puntoJ)) {
                return true;
            }

            boolean intersecta = ((puntoI.lat() > latitud) != (puntoJ.lat() > latitud))
                    && (longitud < (puntoJ.lon() - puntoI.lon()) * (latitud - puntoI.lat())
                            / (puntoJ.lat() - puntoI.lat()) + puntoI.lon());
            if (intersecta) {
                dentro = !dentro;
            }
        }
        return dentro;
    }

    private static boolean puntoSobreSegmento(double latitud, double longitud, Punto inicio, Punto fin) {
        double productoCruzado = (longitud - inicio.lon()) * (fin.lat() - inicio.lat())
                - (latitud - inicio.lat()) * (fin.lon() - inicio.lon());
        if (Math.abs(productoCruzado) > EPSILON) {
            return false;
        }

        double productoEscalar = (longitud - inicio.lon()) * (fin.lon() - inicio.lon())
                + (latitud - inicio.lat()) * (fin.lat() - inicio.lat());
        if (productoEscalar < -EPSILON) {
            return false;
        }

        double longitudSegmento = Math.pow(fin.lon() - inicio.lon(), 2) + Math.pow(fin.lat() - inicio.lat(), 2);
        return productoEscalar <= longitudSegmento + EPSILON;
    }

    private static boolean puntosIguales(Punto a, Punto b) {
        return Math.abs(a.lon() - b.lon()) < EPSILON && Math.abs(a.lat() - b.lat()) < EPSILON;
    }
}
