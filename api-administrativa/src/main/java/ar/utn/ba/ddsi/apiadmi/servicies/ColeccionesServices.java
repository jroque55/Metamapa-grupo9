package ar.utn.ba.ddsi.apiadmi.servicies;

import ar.utn.ba.ddsi.apiadmi.exception.RecursoNoEncontradoException;
import ar.utn.ba.ddsi.apiadmi.models.dtos.ColeccionDto;
import ar.utn.ba.ddsi.apiadmi.models.dtos.CondicionDTO;
import ar.utn.ba.ddsi.apiadmi.models.dtos.input.ColeccionInput;
import ar.utn.ba.ddsi.apiadmi.models.dtos.input.CondicionInput;
import ar.utn.ba.ddsi.apiadmi.models.entities.coleccion.Coleccion;
import ar.utn.ba.ddsi.apiadmi.models.entities.condiciones.*;
import ar.utn.ba.ddsi.apiadmi.models.entities.fuente.Fuente;
import ar.utn.ba.ddsi.apiadmi.models.entities.hecho.Categoria;
import ar.utn.ba.ddsi.apiadmi.models.entities.hecho.Etiqueta;
import ar.utn.ba.ddsi.apiadmi.models.factory.ColeccionFactory;
import ar.utn.ba.ddsi.apiadmi.models.repository.IColeccionRepository;
import ar.utn.ba.ddsi.apiadmi.servicies.interfaces.IColeccionService;
import ar.utn.ba.ddsi.apiadmi.servicies.interfaces.IFuenteServices;
import ar.utn.ba.ddsi.apiadmi.utils.EnumTipoDeAlgoritmo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Long.parseLong;
@Slf4j

@Service
public class ColeccionesServices implements IColeccionService {


    private IColeccionRepository colecciones;
    private IFuenteServices fuenteService;
    private ColeccionFactory coleccionFactory;
    private CategoriaService categoriaService;
    private EtiquetaService etiquetaService;
    private CondicionService condicionService;

    public ColeccionesServices(IColeccionRepository repo, IFuenteServices fuente, ColeccionFactory factory,CondicionService condiciones,CategoriaService serviceCate,EtiquetaService serviceEtiqueta) {
        this.colecciones = repo;
        this.fuenteService = fuente;
        this.coleccionFactory=factory;
        this.condicionService= condiciones;
        this.categoriaService= serviceCate;
        this.etiquetaService = serviceEtiqueta;
    }



    public List<ColeccionDto> obtenerColecciones() {

        log.info("Obteniendo todas las colecciones");
        List<Coleccion> colecciones = this.colecciones.findAll();
        if(colecciones.isEmpty()){
            log.warn("No se encontraron colecciones en la base de datos");
        } else {
            log.debug("Colecciones obtenidas: {}", colecciones.size());
            return this.colecciones.findAll().stream().map(this::ColeccionDto).toList();
        }
        return new ArrayList<>();


    }
/* PARA MOSTRAR TODAS LAS COLECCIONES EN GENERAL*/
    private ColeccionDto ColeccionDto(Coleccion cole) {
        log.debug("Convirtiendo colección a DTO: {}", cole.getTitulo());

        ColeccionDto coleout = new ColeccionDto();
        coleout.setId_coleccion(cole.getId_coleccion());
        coleout.setTitulo(cole.getTitulo());
        coleout.setDescripcion(cole.getDescripcion());
        //coleout.setHandle(cole.getHandle()); /*DEBERIA AGREGARSE*/
        coleout.setCriterios(cole.getCondicionDePertenencia().stream()
                .map(c-> new CondicionDTO(c.getId_condicion(),c.tipo(),c.valor()))
                .collect(Collectors.toList())
        );
        List<Fuente> fuentesInput= new ArrayList<>();
        cole.getFuentes().forEach(fuente -> {
            fuentesInput.add(fuenteService.buscarPorNombre(fuente.getNombre()));
        });
        coleout.setFuentes(fuentesInput);
        coleout.setAlgoritmoDeConsenso(cole.getTipoDeAlgoritmo().toString());
        return coleout;
    }
    /*ESTE PARA PODER TENER TODOS LOS DATOS DE UNA COLECCION EN SI*/
    /*

    private ColeccionDto ColeccionDetailDto(Coleccion cole) {
        ColeccionDto coleout = new ColeccionDto();
        coleout.setTitulo(cole.getTitulo());
        coleout.setDescripcion(cole.getDescripcion());
        //coleout.setHandle(cole.getHandle());
        coleout.setCondiciones(cole.getCondicionDePertenencia().stream()
                .map(InterfaceCondicion::getDetail)
                .collect(Collectors.toList())
        );
//        coleout.addFuentes(
//                cole.getFuentes().stream()
//                        .map(Fuente::getNombre)
//                        .collect(Collectors.toList())
//        );
        return coleout;
    }
     */


    @Override
    public void agregar(ColeccionInput coleccion) {

        log.info("Iniciando creación de colección: {}",
                coleccion.getTitulo());

        List<Fuente> fuentes = coleccion.getFuentes().stream().map(nombre -> {
            log.debug("Buscando fuente: {}", nombre);
            return fuenteService.buscarPorNombre(nombre);
        }).toList();

        log.debug("Fuentes encontradas: {}", fuentes.size());

        log.info("Creando o relacionando criterios para la colección: {}" ,coleccion.getTitulo());
        List<InterfaceCondicion> criterios = coleccion.getCriterios().stream()
                .map(a-> this.cargarOCrearCondicion(a))
                .collect(Collectors.toList());

        log.debug("Condicones relacionadas: {}", criterios.size());

        Coleccion cole = this.coleccionFactory.crearColeccion(coleccion);
        cole.setFuentes(fuentes);
        cole.setTipoDeAlgoritmo(EnumTipoDeAlgoritmo.valueOf(coleccion.getAlgoritmoConcenso()));
        cole.setCondicionDePertenencia(criterios);


        colecciones.save(cole);
        log.info("Colección creada correctamente");
    }

    @Transactional
    @Override
    public void actualizar(Long id,ColeccionInput input){


        log.info("Actualizando colección id={}", id);

        try {


            Coleccion cole = colecciones.findById(id)
                    .orElseThrow(() -> new RuntimeException("No existe la colección"));
            System.out.println(input);

            log.debug("Coleccion encontrada: {}", id);

            cole.setTitulo(input.getTitulo());
            cole.setDescripcion(input.getDescripcion());

            //Actualizacion de algoritmo de concenso
            String algoInput = input.getAlgoritmoConcenso();
            if(algoInput != null) {
                log.info("Actualizando algoritmo de consenso a {} para colección id={}", algoInput, id);
            cole.setTipoDeAlgoritmo(EnumTipoDeAlgoritmo.valueOf(input.getAlgoritmoConcenso()));
            }
            // fuentes
            List<Fuente> nuevasFuentes = input.getFuentes().stream()
                    .map(f -> fuenteService.buscarPorNombre(f))
                    .collect(Collectors.toList());
            cole.setFuentes(nuevasFuentes);
            log.debug("Fuentes actualizadas para colección id={}: {}", id, nuevasFuentes.size());

            List<InterfaceCondicion> condicionesOriginales = cole.getCondicionDePertenencia();

            List<InterfaceCondicion> nuevasCondiciones = input.getCriterios().stream()
                    .map(this::cargarOCrearCondicion)
                    .collect(Collectors.toList());

            // Eliminar de BD las condiciones borradas
            List<InterfaceCondicion> paraEliminar = condicionesOriginales.stream()
                    .filter(cond -> !nuevasCondiciones.contains(cond))
                    .collect(Collectors.toList());

            for (InterfaceCondicion condicion : paraEliminar) {
                this.condicionService.deleteBy(condicion.getId_condicion());
            }

            // *** PASO IMPORTANTE ***
            // Limpiar relaciones (esto elimina filas de condicion_x_coleccion)
            cole.getCondicionDePertenencia().clear();

            // Agregar las nuevas
            log.debug("Condiciones actualizadas de coleccion id={}: {}", id, nuevasCondiciones.size());
            cole.getCondicionDePertenencia().addAll(nuevasCondiciones);
            
            colecciones.save(cole);
        } catch (Exception e) {
            log.error("Error actualizando colección id={}", id, e);

            throw e;
        }
    }

    public InterfaceCondicion cargarOCrearCondicion(CondicionInput inputCOndicion){

        log.debug("Procesando condición tipo={}",inputCOndicion.getTipo());

        if (inputCOndicion.getId() != null) {
            return condicionService.buscarPorId(inputCOndicion.getId());
        }
        // la creo según el tipo
        log.debug("Creando nueva condición del tipo {} con valor {}", inputCOndicion.getTipo(), inputCOndicion.getValor());
        return this.crearCondicion(inputCOndicion);
    }

    @Override
    public Coleccion encontrarPorId(Long id) {

        log.info("Obteniendo la colección con id={}", id);
        Coleccion c = colecciones.findById(id)
                .orElseThrow(() -> {
                    log.error("Colección no encontrada con id={}", id);
                    return new RuntimeException("Colección no encontrada");});

        return c;
    }

    @Override
    public void eliminar(Long id) {
            log.info("Eliminando colección con id={}", id);
            if (!colecciones.existsById(id)) {
                log.error("No se encontró la colección con id={}", id);
                throw new RuntimeException("Colección no encontrada");
            }
            log.debug("Colección encontrada, procediendo a eliminar id={}", id);
            this.colecciones.deleteById(id);


    }

    public InterfaceCondicion crearCondicion(CondicionInput condicionInput) {
        String tipo = condicionInput.getTipo();
        String valor = condicionInput.getValor();
        switch (tipo) {

            case "titulo" :
                //ESTE CASO DEBERIA TRATARSE UN POCO DISTINTO YA QUE SE DEBRIA PODER BUSCAR
                // NO POR IGUAL SI NO QUE LO CONTENGA
                return new CondicionTitulo(valor);


            case "fechaDespues" :
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate desde = LocalDate.parse(valor, formatter);
                return new CondicionFechaDESPUES(desde);

            case "fechaAntes":
                DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate hasta = LocalDate.parse(valor, formatter1);

                return new CondicionFechaANTES(hasta);

            case "categoria":

                Categoria categoria =
                        categoriaService.buscarPorNombre(valor);

                if (categoria == null) {

                    log.warn("Categoría no encontrada: {}", valor);

                    throw new RecursoNoEncontradoException(
                            "Categoría no encontrada: " + valor);
                }

                return new CondicionCategoria(categoria);

            case "etiqueta":
                Etiqueta etiqueta =this.etiquetaService.buscarPorId(valor);
                if(etiqueta==null) {
                    log.warn("Etiqueta no encontrada: {}", valor);

                    throw new RecursoNoEncontradoException(
                            "Etiqueta no encontrada: " + valor);
                }
                return new CondicionEtiqueta(etiqueta);

            case "fuente":
                    Fuente fuente = this.fuenteService.buscarPorId(parseLong(valor));

                    if(fuente==null){
                        log.warn("Fuente no encontrada: {}", valor);

                        throw new RecursoNoEncontradoException(
                                "Fuente no encontrada: " + valor);
                    }

                    return new CondicionFuente(fuente);
            default :
                throw new IllegalArgumentException("Tipo de condición no soportado: " + tipo);
        }
    }


}