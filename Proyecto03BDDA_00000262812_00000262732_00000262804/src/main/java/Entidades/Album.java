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
    private String genero;
    private String imagenPortada;
    private ObjectId idArtista;
    private String tipoArtista;
    private List<ObjectId> canciones;

    public Album() {
    }
}
