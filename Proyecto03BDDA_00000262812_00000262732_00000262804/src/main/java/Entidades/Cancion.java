/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import org.bson.types.ObjectId;

/**
 *
 * @author Andre
 */
public class Cancion {
    private ObjectId id;
    private String nombre;
    private String duracion;
    private ObjectId idAlbum;

    public Cancion() {
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

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public ObjectId getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(ObjectId idAlbum) {
        this.idAlbum = idAlbum;
    }
    
    
}
