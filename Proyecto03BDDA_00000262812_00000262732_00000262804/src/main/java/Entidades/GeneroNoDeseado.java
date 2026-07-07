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
    private ObjectId idGND;
    private LocalDate fechaAgregacion;

    public GeneroNoDeseado() {
    }

    public GeneroNoDeseado(ObjectId idGND, LocalDate fechaAgregacion) {
        this.idGND = idGND;
        this.fechaAgregacion = fechaAgregacion;
    }

    public ObjectId getIdGND() {
        return idGND;
    }

    public void setIdGND(ObjectId idGND) {
        this.idGND = idGND;
    }

    public LocalDate getFechaAgregacion() {
        return fechaAgregacion;
    }

    public void setFechaAgregacion(LocalDate fechaAgregacion) {
        this.fechaAgregacion = fechaAgregacion;
    }
    
}
