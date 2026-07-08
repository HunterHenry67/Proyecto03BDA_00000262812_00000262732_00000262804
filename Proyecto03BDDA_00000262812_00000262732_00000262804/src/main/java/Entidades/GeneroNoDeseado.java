/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import java.time.LocalDate;
import org.bson.types.ObjectId;

/**
 *
 * @author BALAMRUSH
 */
public class GeneroNoDeseado {
    private ObjectId idGenero;
    private String nombreGenero;
    private LocalDate fechaAgregacion;

    public GeneroNoDeseado() {
    }

    public GeneroNoDeseado(ObjectId idGenero, String nombreGenero, LocalDate fechaAgregacion) {
        this.idGenero = idGenero;
        this.nombreGenero = nombreGenero;
        this.fechaAgregacion = fechaAgregacion;
    }

    public ObjectId getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(ObjectId idGenero) {
        this.idGenero = idGenero;
    }

    public String getNombreGenero() {
        return nombreGenero;
    }

    public void setNombreGenero(String nombreGenero) {
        this.nombreGenero = nombreGenero;
    }

    public LocalDate getFechaAgregacion() {
        return fechaAgregacion;
    }

    public void setFechaAgregacion(LocalDate fechaAgregacion) {
        this.fechaAgregacion = fechaAgregacion;
    }

}
