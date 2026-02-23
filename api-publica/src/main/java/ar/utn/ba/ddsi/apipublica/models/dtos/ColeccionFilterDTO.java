package ar.utn.ba.ddsi.apipublica.models.dtos;

import java.util.ArrayList;
import java.util.List;

public class ColeccionFilterDTO {
    private String titulo;
    private String descripcion;
    private String tipoAlgoritmo;
    // Puede recibirse una lista de ids (repetidos) o una única cadena separada por comas
    private List<String> fuenteIds; // raw strings como vinieron del request

    public ColeccionFilterDTO() {}

    public ColeccionFilterDTO(String titulo, String descripcion, String tipoAlgoritmo, List<String> fuenteIds) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipoAlgoritmo = tipoAlgoritmo;
        this.fuenteIds = fuenteIds;
    }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getTipoAlgoritmo() { return tipoAlgoritmo; }
    public void setTipoAlgoritmo(String tipoAlgoritmo) { this.tipoAlgoritmo = tipoAlgoritmo; }
    public List<String> getFuenteIds() { return fuenteIds; }
    public void setFuenteIds(List<String> fuenteIds) { this.fuenteIds = fuenteIds; }

    // Validación simple: si se pasó un id de fuente, debe ser numérico
    /**
     * Parsea la/s fuentes recibidas (posible lista o cadena con comas) y devuelve una lista de Long.
     * Retorna null si no se envió ninguna fuente.
     */
    public List<Long> parseFuenteIdsOrNull() {
        if (this.fuenteIds == null || this.fuenteIds.isEmpty()) return null;
        List<Long> out = new ArrayList<>();
        for (String raw : this.fuenteIds) {
            if (raw == null) continue;
            String[] parts = raw.split(",");
            for (String p : parts) {
                String t = p.trim();
                if (t.isEmpty()) continue;
                try {
                    out.add(Long.parseLong(t));
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("fuente_id inválido: debe ser un número -> '" + t + "'");
                }
            }
        }
        if (out.isEmpty()) return null;
        return out;
    }
}
