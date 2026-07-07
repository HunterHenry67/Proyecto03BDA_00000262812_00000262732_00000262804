/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import java.time.LocalDate;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author Andre
 */
public class Album {
    private ObjectId id;
    private String nombre;
    private LocalDate fechaLanzamiento;
    private ObjectId idGenero;
    private String imagenPortada;
    private List<Cancion> canciones;

    public Album() {
    }

    public Album(ObjectId id, String nombre, LocalDate fechaLanzamiento, ObjectId idGenero, String imagenPortada, List<Cancion> canciones) {
        this.id = id;
        this.nombre = nombre;
        this.fechaLanzamiento = fechaLanzamiento;
        this.idGenero = idGenero;
        this.imagenPortada = imagenPortada;
        this.canciones = canciones;
    }

    public ObjectId getId() {
        return id;
    }
    
    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public void setFechaLanzamiento(LocalDate fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public ObjectId getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(ObjectId idGenero) {
        this.idGenero = idGenero;
    }

    public String getImagenPortada() {
        return imagenPortada;
    }

    public void setImagenPortada(String imagenPortada) {
        this.imagenPortada = imagenPortada;
    }

    public List<Cancion> getCanciones() {
        return canciones;
    }

    public void setCanciones(List<Cancion> canciones) {
        this.canciones = canciones;
    }
    
    
}
