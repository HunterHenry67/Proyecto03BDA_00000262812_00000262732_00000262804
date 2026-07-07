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
public class AlbumFavorito {
    private ObjectId idAlbum;
    private LocalDate fechaAgregacion;

    public AlbumFavorito() {
    }

    public AlbumFavorito(ObjectId idAlbum, LocalDate fechaAgregacion) {
        this.idAlbum = idAlbum;
        this.fechaAgregacion = fechaAgregacion;
    }

    public ObjectId getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(ObjectId idAlbum) {
        this.idAlbum = idAlbum;
    }

    public LocalDate getFechaAgregacion() {
        return fechaAgregacion;
    }

    public void setFechaAgregacion(LocalDate fechaAgregacion) {
        this.fechaAgregacion = fechaAgregacion;
    }
    
    
}
