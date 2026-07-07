/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author Andre
 */
public class Favorito {
    private ObjectId id;
    private ObjectId idUsuario;
    private List<ArtistaFavorito> artistas;
    private List<AlbumFavorito> albumes;
    private List<CancionFavorita> canciones;

    public Favorito() {
    }

    public Favorito(ObjectId id, ObjectId idUsuario, List<ArtistaFavorito> artistas, List<AlbumFavorito> albumes, List<CancionFavorita> canciones) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.artistas = artistas;
        this.albumes = albumes;
        this.canciones = canciones;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(ObjectId idUsuario) {
        this.idUsuario = idUsuario;
    }

    public List<ArtistaFavorito> getArtistas() {
        return artistas;
    }

    public void setArtistas(List<ArtistaFavorito> artistas) {
        this.artistas = artistas;
    }

    public List<AlbumFavorito> getAlbumes() {
        return albumes;
    }

    public void setAlbumes(List<AlbumFavorito> albumes) {
        this.albumes = albumes;
    }

    public List<CancionFavorita> getCanciones() {
        return canciones;
    }

    public void setCanciones(List<CancionFavorita> canciones) {
        this.canciones = canciones;
    }
    
    
}
