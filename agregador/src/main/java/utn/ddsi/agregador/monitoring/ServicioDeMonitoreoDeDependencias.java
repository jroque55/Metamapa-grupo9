package utn.ddsi.agregador.monitoring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import utn.ddsi.agregador.monitoring.healthindicators.DatabaseHealthIndicator;
import utn.ddsi.agregador.monitoring.healthindicators.FuenteDinamicaHealthIndicator;
import utn.ddsi.agregador.monitoring.healthindicators.FuenteEstaticaHealthIndicator;
import utn.ddsi.agregador.monitoring.healthindicators.FuenteProxyHealthIndicator;

@Slf4j
@Service
public class ServicioDeMonitoreoDeDependencias {
    //Contadores y Maximos para alertas
    private int fallasCriticos = 0;

    @Value("${monitoreo.max-fallos:3}")
    private int MAX_FALLOS;

    @Value("${monitoreo.force-exit-on-critical:true}")
    private boolean forceExitOnCritical;

    //HealthIndicator para cada dependencia
    private final DatabaseHealthIndicator database;
    private final FuenteDinamicaHealthIndicator dinamica;
    private final FuenteEstaticaHealthIndicator estatica;
    //private final FuenteProxyHealthIndicator proxy;

    public ServicioDeMonitoreoDeDependencias(DatabaseHealthIndicator database,
                                             FuenteDinamicaHealthIndicator dinamica,
                                             FuenteEstaticaHealthIndicator estatica
                                             ) {
        this.database = database;
        this.dinamica = dinamica;
        this.estatica = estatica;
        //this.proxy = proxy;
    }

    private void manejarFallaCritica() {
        fallasCriticos++;
        if(fallasCriticos >= MAX_FALLOS) {
            log.error("Falla crítica persistente -> forzando restart (forceExitOnCritical={})", forceExitOnCritical);
            if (forceExitOnCritical) {
                // Lanzamos una excepción no controlada para que la plataforma pueda reiniciar el pod/servicio
                throw new IllegalStateException("AutoRestart por fallas críticas persistentes");
            } else {
                log.error("Se alcanzó el máximo de fallas críticas ({}), pero forceExitOnCritical=false. Revisar manualmente.", MAX_FALLOS);
            }
        } else {
            log.warn("Falla crítica detectada en dependencias esenciales. Se han registrado {} fallas consecutivas. Se recomienda revisar las dependencias críticas.", fallasCriticos);
        }
    }

    @Scheduled(fixedDelay = 15000)
    public void heartbeat() {
        try {
            boolean databaseOk = database.estaDisponible();
            boolean dinamicaOk = dinamica.estaDisponible();
            boolean estaticaOk = estatica.estaDisponible();
            //boolean proxyOk = proxy.estaDisponible();

            log.debug("Estado dependencias - database: {}, dinamica: {}, estatica: {}",
                    databaseOk, dinamicaOk, estaticaOk);

            if (!databaseOk) {
                log.error("Dependencia crítica 'database' DOWN -> marcando y manejando");
                database.markDown();
            } else {
                database.markUp();
            }

            if (!dinamicaOk) {
                log.error("Dependencia crítica 'fuenteDinamica' DOWN -> marcando y manejando");
                dinamica.markDown();
            } else {
                dinamica.markUp();
            }

            if(!estaticaOk) {
                log.warn("Dependencia 'fuenteEstatica' DOWN -> marcando");
                estatica.markDown();
            } else {
                estatica.markUp();
            }

            //if(!proxyOk) {
            //    log.warn("Dependencia 'fuenteProxy' DOWN -> marcando");
            //    proxy.markDown();
            //} else {
            //    proxy.markUp();
            //}

            if (!databaseOk || !dinamicaOk ) {
                manejarFallaCritica();
            }
            if(!estaticaOk ){
                log.warn("Alerta: Fuente Estática o Proxy no disponibles. Se recomienda revisar estas dependencias.");
            }
            if(databaseOk && dinamicaOk && estaticaOk){
                fallasCriticos = 0; // resetear contador al recuperarse
                log.info("Heartbeat OK – todas las dependencias UP");
            }
        } catch (Exception ex) {
            // Capturamos cualquier excepción para que quede en logs y sea visible para Render
            log.error("Excepción en heartbeat de monitoreo: {}", ex.getMessage(), ex);
            // Re-lanzamos si forceExitOnCritical para que la plataforma reinicie
            if (forceExitOnCritical) {
                throw ex;
            }
        }
    }

}
