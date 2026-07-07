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
public class ArtistaFavorito {
    private ObjectId idArtista;
    private LocalDate fechaAgregacion;

    public ArtistaFavorito() {
    }

    public ArtistaFavorito(ObjectId idArtista, LocalDate fechaAgregacion) {
        this.idArtista = idArtista;
        this.fechaAgregacion = fechaAgregacion;
    }

    public ObjectId getIdArtista() {
        return idArtista;
    }

    public void setIdArtista(ObjectId idArtista) {
        this.idArtista = idArtista;
    }

    public LocalDate getFechaAgregacion() {
        return fechaAgregacion;
    }

    public void setFechaAgregacion(LocalDate fechaAgregacion) {
        this.fechaAgregacion = fechaAgregacion;
    }  
}
