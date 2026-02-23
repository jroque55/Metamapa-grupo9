package ar.utn.ba.ddsi.fuenteproxy.models.entities;

public class Adjunto {
    private TipoMedia tipo;
    private String url;
    public Adjunto(TipoMedia tipo, String url) {
        this.tipo = tipo;
        this.url = url;
    }
    public TipoMedia getTipo() {
        return tipo;
    }
    public String getUrl() {
        return url;}
    public void setTipo(TipoMedia tipo) {
        this.tipo = tipo;
    }
    public void setUrl(String url) {
        this.url = url;}
}
