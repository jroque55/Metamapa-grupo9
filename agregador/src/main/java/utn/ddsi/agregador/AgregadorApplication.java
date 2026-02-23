package utn.ddsi.agregador;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import utn.ddsi.agregador.domain.agregador.ActualizadorColecciones;
import utn.ddsi.agregador.domain.agregador.FiltradorDeHechos;
import utn.ddsi.agregador.domain.agregador.Normalizador;
import utn.ddsi.agregador.domain.agregador.Scheduler;
import utn.ddsi.agregador.domain.fuentes.Loader;
import utn.ddsi.agregador.domain.fuentes.LoaderDinamico;
import utn.ddsi.agregador.domain.fuentes.LoaderEstatico;
import utn.ddsi.agregador.domain.solicitudEliminacion.DetectorBasicoDeSpam;
import utn.ddsi.agregador.domain.solicitudEliminacion.GestorDeSolicitudes;
import utn.ddsi.agregador.repository.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class AgregadorApplication {

    public static void main(String[] args) {

        log.info("Iniciando aplicacion Agregador");
        ApplicationContext ctx = SpringApplication.run(AgregadorApplication.class, args);
        IRepositoryProvincias repoProv = ctx.getBean(IRepositoryProvincias.class);
        IRepositoryCategorias repoCat = ctx.getBean(IRepositoryCategorias.class);
        IRepositoryHechos repoHecho = ctx.getBean(IRepositoryHechos.class);
        IRepositoryColecciones repoCol = ctx.getBean(IRepositoryColecciones.class);
        IRepositorySolicitudes repoSol = ctx.getBean(IRepositorySolicitudes.class);
        IRepositoryHechoXColeccion repoHxC = ctx.getBean(IRepositoryHechoXColeccion.class);
        IRepositoryUbicacion repoUbi = ctx.getBean(IRepositoryUbicacion.class);
        IRepositoryFuentes repoFu = ctx.getBean(IRepositoryFuentes.class);

        //LoaderEstatico loaderEs = ctx.getBean(LoaderEstatico.class);
        //LoaderDinamico loaderDin = ctx.getBean(LoaderDinamico.class);

        FiltradorDeHechos filter = new FiltradorDeHechos();
        Normalizador normalizador = new Normalizador(repoCat, repoProv, repoUbi);
        DetectorBasicoDeSpam detectorBasico = new DetectorBasicoDeSpam();
        List<Loader> loaders = new ArrayList<>();
        //loaders.add(loaderEs);
        //loaders.add(loaderDin);
        GestorDeSolicitudes gestBasico = new GestorDeSolicitudes(repoSol, detectorBasico);
        ActualizadorColecciones act = new ActualizadorColecciones(repoCol, repoHecho, normalizador, gestBasico, loaders, filter, repoHxC, repoFu);
        Scheduler scheduler = ctx.getBean(Scheduler.class);
        //scheduler.ejecutarActualizacionPeriodica();
        scheduler.ejecutarAlgoritmosDeConsenso();
        log.info("Aplicacion Agregador iniciada correctamente");

    }
}
