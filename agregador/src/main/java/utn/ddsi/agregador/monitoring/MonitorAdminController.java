package utn.ddsi.agregador.monitoring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utn.ddsi.agregador.monitoring.healthindicators.DatabaseHealthIndicator;
import utn.ddsi.agregador.monitoring.healthindicators.FuenteDinamicaHealthIndicator;
import utn.ddsi.agregador.monitoring.healthindicators.FuenteEstaticaHealthIndicator;
//import utn.ddsi.agregador.monitoring.healthindicators.FuenteProxyHealthIndicator;

@Slf4j
@RestController
@RequestMapping("/monitor")
public class MonitorAdminController {

    private final DatabaseHealthIndicator database;
    private final FuenteDinamicaHealthIndicator dinamica;
    private final FuenteEstaticaHealthIndicator estatica;
    //private final FuenteProxyHealthIndicator proxy;

    public MonitorAdminController(
            DatabaseHealthIndicator database,
            FuenteDinamicaHealthIndicator dinamica,
            FuenteEstaticaHealthIndicator estatica) {

        this.database = database;
        this.dinamica = dinamica;
        this.estatica = estatica;
        //this.proxy = proxy;
    }

    // ---------- FALLAS ----------

    @PostMapping("/fail/database")
    public void failDatabase() {
        log.info("Simulando falla en la Base de Datos");
        database.forceDown();
    }

    @PostMapping("/fail/fuente-dinamica")
    public void failFuenteDinamica() {
        log.info("Simulando falla en la Fuente Dinámica");
        dinamica.forceDown();
    }

    @PostMapping("/fail/fuente-estatica")
    public void failFuenteEstatica() {
        log.info("Simulando falla en la Fuente Estática");
        estatica.forceDown();
    }

    //@PostMapping("/fail/fuente-proxy")
    //public void failFuenteProxy() {
    //    log.info("Simulando falla en la Fuente Proxy");
    //    proxy.forceDown();
    //}

    // ---------- RECUPERACIÓN ----------

    @PostMapping("/recover/all")
    public void recoverAll() {
        log.info("Simulando recuperación de todas las dependencias");
        database.recover();
        dinamica.recover();
        estatica.recover();
        //proxy.recover();
    }
}
