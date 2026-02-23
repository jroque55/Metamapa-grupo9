package utn.ddsi.agregador.domain.solicitudEliminacion;


public interface DetectorDeSpam {
    boolean esSpam(String texto);
}