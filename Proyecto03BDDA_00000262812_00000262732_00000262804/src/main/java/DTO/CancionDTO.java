/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author user
 */
public class CancionDTO {    
    private String idCancion;
    private String nombre;
    private String duracion;
    private String idGenero;

    public CancionDTO() {
    }

    public CancionDTO(String nombre, String duracion, String idGenero) {
        this.nombre = nombre;
        this.duracion = duracion;
        this.idGenero = idGenero;
    }

    public CancionDTO(String idCancion, String nombre, String duracion, String idGenero) {
        this.idCancion = idCancion;
        this.nombre = nombre;
        this.duracion = duracion;
        this.idGenero = idGenero;
    }

    public String getIdCancion() {
        return idCancion;
    }

    public void setIdCancion(String idCancion) {
        this.idCancion = idCancion;
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

    public String getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(String idGenero) {
        this.idGenero = idGenero;
    }
}
