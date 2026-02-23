package ar.utn.ba.ddsi.apipublica.models.dtos;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class HechoFilterDTO {
    private String categoria;
    private String fechaReporteDesde;
    private String fechaReporteHasta;
    private String fechaAcontecimientoDesde;
    private String fechaAcontecimientoHasta;
    private String provincia;
    private String textoLibre;
    private String tipoFuente;

    // Campos parseados (resultado de la validación)
    private LocalDate fechaReporteDesdeParsed;
    private LocalDate fechaReporteHastaParsed;
    private LocalDate fechaAcontecimientoDesdeParsed;
    private LocalDate fechaAcontecimientoHastaParsed;
    private Float ubicacionLatitudParsed;
    private Float ubicacionLongitudParsed;
    private String tipoFuenteParsed;

    // Constructor sin-argumentos necesario para usos donde no se pasan filtros
    public HechoFilterDTO() {}

    public HechoFilterDTO(
            String categoria,
            String fechaReporteDesde,
            String fechaReporteHasta,
            String fechaAcontecimientoDesde,
            String fechaAcontecimientoHasta,
            String provincia,
            String textoLibre,
            String tipoFuente
    ) {
        this.categoria = categoria;
        this.fechaReporteDesde = fechaReporteDesde;
        this.fechaReporteHasta = fechaReporteHasta;
        this.fechaAcontecimientoDesde = fechaAcontecimientoDesde;
        this.fechaAcontecimientoHasta = fechaAcontecimientoHasta;
        this.provincia = provincia;
        this.textoLibre = textoLibre;
        this.tipoFuente= tipoFuente;
    }

    public String getTipoFuente() {
        return tipoFuente;
    }

    public void setTipoFuente(String tipoFuente) {
        this.tipoFuente = tipoFuente;
    }

    public void setFechaReporteDesdeParsed(LocalDate fechaReporteDesdeParsed) {
        this.fechaReporteDesdeParsed = fechaReporteDesdeParsed;
    }

    public void setFechaReporteHastaParsed(LocalDate fechaReporteHastaParsed) {
        this.fechaReporteHastaParsed = fechaReporteHastaParsed;
    }

    public void setFechaAcontecimientoDesdeParsed(LocalDate fechaAcontecimientoDesdeParsed) {
        this.fechaAcontecimientoDesdeParsed = fechaAcontecimientoDesdeParsed;
    }

    public void setFechaAcontecimientoHastaParsed(LocalDate fechaAcontecimientoHastaParsed) {
        this.fechaAcontecimientoHastaParsed = fechaAcontecimientoHastaParsed;
    }

    public void setUbicacionLatitudParsed(Float ubicacionLatitudParsed) {
        this.ubicacionLatitudParsed = ubicacionLatitudParsed;
    }

    public void setUbicacionLongitudParsed(Float ubicacionLongitudParsed) {
        this.ubicacionLongitudParsed = ubicacionLongitudParsed;
    }

    public String getTipoFuenteParsed() {
        return tipoFuenteParsed;
    }

    public void setTipoFuenteParsed(String tipoFuenteParsed) {
        this.tipoFuenteParsed = tipoFuenteParsed;
    }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getFechaReporteDesde() { return fechaReporteDesde; }
    public void setFechaReporteDesde(String fechaReporteDesde) { this.fechaReporteDesde = fechaReporteDesde; }

    public String getFechaReporteHasta() { return fechaReporteHasta; }
    public void setFechaReporteHasta(String fechaReporteHasta) { this.fechaReporteHasta = fechaReporteHasta; }

    public String getFechaAcontecimientoDesde() { return fechaAcontecimientoDesde; }
    public void setFechaAcontecimientoDesde(String fechaAcontecimientoDesde) { this.fechaAcontecimientoDesde = fechaAcontecimientoDesde; }

    public String getFechaAcontecimientoHasta() { return fechaAcontecimientoHasta; }
    public void setFechaAcontecimientoHasta(String fechaAcontecimientoHasta) { this.fechaAcontecimientoHasta = fechaAcontecimientoHasta; }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(){
        this.provincia = provincia;
    }

    public String getTextoLibre() { return textoLibre; }
    public void setTextoLibre(String textoLibre) { this.textoLibre = textoLibre; }

    // --- Getters para campos parseados ---
    public LocalDate getFechaReporteDesdeParsed() { return fechaReporteDesdeParsed; }
    public LocalDate getFechaReporteHastaParsed() { return fechaReporteHastaParsed; }
    public LocalDate getFechaAcontecimientoDesdeParsed() { return fechaAcontecimientoDesdeParsed; }
    public LocalDate getFechaAcontecimientoHastaParsed() { return fechaAcontecimientoHastaParsed; }
    public Float getUbicacionLatitudParsed() { return ubicacionLatitudParsed; }
    public Float getUbicacionLongitudParsed() { return ubicacionLongitudParsed; }

    /**
     * Valida y parsea los campos String del DTO. Si hay un formato inválido lanza
     * IllegalArgumentException con un mensaje claro. Al finalizar, los campos
     * parseados quedan disponibles mediante los getters correspondientes.
     */
    public void validateAndParse() {
        // Parseo de fechas
        try {
            if (this.fechaReporteDesde != null && !this.fechaReporteDesde.isBlank())
                this.fechaReporteDesdeParsed = LocalDate.parse(this.fechaReporteDesde);
            if (this.fechaReporteHasta != null && !this.fechaReporteHasta.isBlank())
                this.fechaReporteHastaParsed = LocalDate.parse(this.fechaReporteHasta);
            if (this.fechaAcontecimientoDesde != null && !this.fechaAcontecimientoDesde.isBlank())
                this.fechaAcontecimientoDesdeParsed = LocalDate.parse(this.fechaAcontecimientoDesde);
            if (this.fechaAcontecimientoHasta != null && !this.fechaAcontecimientoHasta.isBlank())
                this.fechaAcontecimientoHastaParsed = LocalDate.parse(this.fechaAcontecimientoHasta);
        } catch (DateTimeParseException dte) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use yyyy-MM-dd", dte);
        }
//
//        // Parseo de ubicacion
//        try {
//            if (this.ubicacionLatitud != null && !this.ubicacionLatitud.isBlank())
//                this.ubicacionLatitudParsed = Float.parseFloat(this.ubicacionLatitud);
//            if (this.ubicacionLongitud != null && !this.ubicacionLongitud.isBlank())
//                this.ubicacionLongitudParsed = Float.parseFloat(this.ubicacionLongitud);
//        } catch (NumberFormatException nfe) {
//            throw new IllegalArgumentException("Latitud o longitud inválida", nfe);
//        }

        // Validaciones lógicas: desde <= hasta si ambos existen
        if (this.fechaReporteDesdeParsed != null && this.fechaReporteHastaParsed != null) {
            if (this.fechaReporteDesdeParsed.isAfter(this.fechaReporteHastaParsed)) {
                throw new IllegalArgumentException("fecha_reporte_desde no puede ser posterior a fecha_reporte_hasta");
            }
        }
        if (this.fechaAcontecimientoDesdeParsed != null && this.fechaAcontecimientoHastaParsed != null) {
            if (this.fechaAcontecimientoDesdeParsed.isAfter(this.fechaAcontecimientoHastaParsed)) {
                throw new IllegalArgumentException("fecha_acontecimiento_desde no puede ser posterior a fecha_acontecimiento_hasta");
            }
        }

        // Si una de las coordenadas está presente pero la otra no -> error
        if ((this.ubicacionLatitudParsed != null && this.ubicacionLongitudParsed == null) ||
                (this.ubicacionLatitudParsed == null && this.ubicacionLongitudParsed != null)) {
            throw new IllegalArgumentException("Debe suministrar ambas coordenadas: ubicacion_latitud y ubicacion_longitud");
        }
    }

    /**
     * Indica si no se pasó ningún criterio de filtrado.
     */
    public boolean isEmpty() {
        return ( (this.categoria == null || this.categoria.isBlank())
                && this.fechaReporteDesdeParsed == null
                && this.fechaReporteHastaParsed == null
                && this.fechaAcontecimientoDesdeParsed == null
                && this.fechaAcontecimientoHastaParsed == null
                && this.ubicacionLatitudParsed == null
                && this.ubicacionLongitudParsed == null
                && this.tipoFuente==null
        );
    }
}
