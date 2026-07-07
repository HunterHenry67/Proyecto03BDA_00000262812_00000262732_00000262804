/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author BALAMRUSH
 */
public class Artista {
    private ObjectId id;
    private String nombre;
    private String imagen;
    private ObjectId idGenero;
    private List<Integrante> integrantes;
    private List<Album> albumes;

    public Artista() {
    }

    public Artista(ObjectId id, String nombre, String imagen, ObjectId idGenero, List<Integrante> integrantes, List<Album> albumes) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
        this.idGenero = idGenero;
        this.integrantes = integrantes;
        this.albumes = albumes;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public ObjectId getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(ObjectId idGenero) {
        this.idGenero = idGenero;
    }

    public List<Integrante> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(List<Integrante> integrantes) {
        this.integrantes = integrantes;
    }

    public List<Album> getAlbumes() {
        return albumes;
    }

    public void setAlbumes(List<Album> albumes) {
        this.albumes = albumes;
    }
    
    
}
