/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author BALAMRUSH
 */
public class ArtistaDTO {
     private String id;
    private String nombre;
    private String imagen;
    private ObjectId idGenero;
    private List<IntegranteDTO> integrantes;
    private List<AlbumDTO> albumes;

    public ArtistaDTO() {
    }

    public ArtistaDTO(String nombre, String imagen, ObjectId idGenero, List<IntegranteDTO> integrantes,List<AlbumDTO> albumes) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.idGenero = idGenero;
        this.integrantes = integrantes;
        this.albumes = albumes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public List<IntegranteDTO> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(List<IntegranteDTO> integrantes) {
        this.integrantes = integrantes;
    }

    public List<AlbumDTO> getAlbumes() {
        return albumes;
    }

    public void setAlbumes(List<AlbumDTO> albumes) {
        this.albumes = albumes;
    }
}
