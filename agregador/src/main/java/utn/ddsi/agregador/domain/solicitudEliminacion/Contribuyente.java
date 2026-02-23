/*package utn.ddsi.agregador.domain.solicitudEliminacion;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Contribuyente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_contribuyente;

    private String nombre;
    private String apellido;
    private int edad;
    private Boolean anonimo;

    public Contribuyente() {}

    public Contribuyente(Long id, String nombre, String apellido, int edad) {
        if (edad < 0) {
            throw new IllegalArgumentException("La edad no puede ser negativa.");
        }
        this.id_contribuyente = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.anonimo = false;
    }

    public Contribuyente(String nombre, String apellido, int edad) {
        this(null, nombre, apellido, edad);
    }
}
*/