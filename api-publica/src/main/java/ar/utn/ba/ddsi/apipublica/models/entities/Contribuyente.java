/*package ar.utn.ba.ddsi.apipublica.models.entities;

import jakarta.persistence.*;

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

    public Long getId_contribuyente() { return id_contribuyente; }
    public void setId_contribuyente(Long id_contribuyente) { this.id_contribuyente = id_contribuyente; }

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getApellido() {return apellido;}
    public void setApellido(String apellido) {this.apellido = apellido;}

    public int getEdad() {return edad;}
    public void setEdad(int edad) {this.edad = edad;}

    public Boolean getAnonimo() {return anonimo;}
    public void setAnonimo(Boolean anonimo) {this.anonimo = anonimo;}

}

 */
