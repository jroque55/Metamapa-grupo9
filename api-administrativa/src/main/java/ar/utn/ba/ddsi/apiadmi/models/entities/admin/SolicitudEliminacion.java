package ar.utn.ba.ddsi.apiadmi.models.entities.admin;

import ar.utn.ba.ddsi.apiadmi.models.entities.hecho.Hecho;
import ar.utn.ba.ddsi.apiadmi.utils.EnumEstadoSol;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "solicitud")
public class SolicitudEliminacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_solicitud;
    @ManyToOne
    private Hecho hecho;
    @Column(nullable = false)
    private LocalDate fecha;
    @Column(length = 200)
    private String motivo;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumEstadoSol estado;
    @Column(nullable = false)
    private Long id_solicitante;
    @Column
    private boolean spam;

    public SolicitudEliminacion(Long id_solicitante,Hecho hecho, LocalDate fecha, String motivo) {
        this.id_solicitante = id_solicitante;
        this.hecho = hecho;
        this.fecha = fecha;
        this.motivo = motivo;
        this.estado = EnumEstadoSol.PENDIENTE;
    }

    public SolicitudEliminacion() {

    }
}
