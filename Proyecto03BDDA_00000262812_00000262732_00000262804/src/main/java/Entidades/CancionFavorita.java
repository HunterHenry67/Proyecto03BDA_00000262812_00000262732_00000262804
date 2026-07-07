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
public class CancionFavorita {
    private ObjectId idCancion;
    private LocalDate fechaAgregacion;

    public CancionFavorita() {
    }

    public CancionFavorita(ObjectId idCancion, LocalDate fechaAgregacion) {
        this.idCancion = idCancion;
        this.fechaAgregacion = fechaAgregacion;
    }

    public ObjectId getIdCancion() {
        return idCancion;
    }

    public void setIdCancion(ObjectId idCancion) {
        this.idCancion = idCancion;
    }

    public LocalDate getFechaAgregacion() {
        return fechaAgregacion;
    }

    public void setFechaAgregacion(LocalDate fechaAgregacion) {
        this.fechaAgregacion = fechaAgregacion;
    }
    
    
}
