package ar.utn.ba.ddsi.apipublica.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@Entity
@Table(name = "solicitud")
@NoArgsConstructor
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
//VER SI ESTO VA ACA O EN OTRA PARTE
    @Column(nullable = false)
    private Long id_solicitante;
    @Column
    private boolean spam;

    public SolicitudEliminacion(Long solicitante,Hecho hecho, LocalDate fecha, String motivo) {
        this.id_solicitante = solicitante;
        this.hecho = hecho;
        this.fecha = fecha;
        this.motivo = motivo;
        this.estado = EnumEstadoSol.PENDIENTE;
    }
    public void get(){
        getMotivo();
    }
}