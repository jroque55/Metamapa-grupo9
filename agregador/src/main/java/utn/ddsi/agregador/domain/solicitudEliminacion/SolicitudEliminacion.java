package utn.ddsi.agregador.domain.solicitudEliminacion;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utn.ddsi.agregador.domain.hecho.Hecho;
import utn.ddsi.agregador.utils.EnumEstadoSol;

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
    @Column(nullable = false)
    private Long id_solicitante;
    //podria ser null?
    @Column 
    private boolean spam;

    public SolicitudEliminacion(Hecho hecho, LocalDate fecha, String motivo) {
        this.hecho = hecho;
        this.fecha = fecha;
        this.motivo = motivo;
        this.estado = EnumEstadoSol.PENDIENTE;
        this.spam =  false;
    }
}