package ar.utn.ba.ddsi.apipublica.models.dtos;

import ar.utn.ba.ddsi.apipublica.models.entities.Adjunto;

public class AdjuntoDTO {
    private String url;
    private String tipoMedia;

    public AdjuntoDTO() {
    }

    public AdjuntoDTO(Adjunto adjunto) {
        this.url = adjunto.getUrl();
        this.tipoMedia = adjunto.getTipo().name();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTipoMedia() {
        return tipoMedia;
    }

    public void setTipoMedia(String tipoMedia) {
        this.tipoMedia = tipoMedia;
    }


}
