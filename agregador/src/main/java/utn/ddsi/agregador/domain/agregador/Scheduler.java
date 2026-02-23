package utn.ddsi.agregador.domain.agregador;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class Scheduler {

    private final ActualizadorColecciones actualizador;

    public Scheduler(ActualizadorColecciones actualizador) {
        this.actualizador = actualizador;
    }

    // Ejecuta cada hora

    @Scheduled(fixedRate = 3600000)
    public void ejecutarActualizacionPeriodica() {
        try {
            log.info("Scheduler: Iniciando actualización periódica de colecciones");
            actualizador.actualizarColecciones();
            log.info("Scheduler: Actualización periódica completada");
        } catch (Exception e) {
            log.error("Scheduler: Error en actualización periódica: {}", e.getMessage(), e);

        }
    }
    @Scheduled(cron = "0 */15 * * * *")
    public void procesarSolcitudesSpam() {
        try {
            log.info("Scheduler: Iniciando procesamiento de solicitudes de eliminación");
            actualizador.getGestorSolicitudes().procesarTodasLasSolicitudes();
        } catch (Exception e) {
            log.error("[Scheduler] Error al procesar Solicitud Spam: {}", e.getMessage());
        }
    }
    @Scheduled(cron = "0 0 1 * * *")
    public void ejecutarAlgoritmosDeConsenso() {
        try {

            log.info("Scheduler: Iniciando ejecución de algoritmos de consenso");
            actualizador.ejecutarAlgoritmosDeConsenso();
        } catch (Exception e) {
            log.error("[Scheduler] Error al ejecutar Algoritmo de concenso: {}" , e.getMessage());
        }
    }
}