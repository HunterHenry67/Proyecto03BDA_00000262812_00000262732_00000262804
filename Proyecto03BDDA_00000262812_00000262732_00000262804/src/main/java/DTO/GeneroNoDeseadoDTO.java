/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import java.time.LocalDate;

/**
 *
 * @author Andre
 */

public class GeneroNoDeseadoDTO {

    private String idGenero;
    private String nombreGenero;
    private LocalDate fechaAgregacion;

    public GeneroNoDeseadoDTO() {
    }

    public GeneroNoDeseadoDTO(String idGenero, String nombreGenero, LocalDate fechaAgregacion) {
        this.idGenero = idGenero;
        this.nombreGenero = nombreGenero;
        this.fechaAgregacion = fechaAgregacion;
    }

    public String getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(String idGenero) {
        this.idGenero = idGenero;
    }

    public String getNombreGenero() {
        return nombreGenero;
    }

    public void setNombreGenero(String nombreGenero) {
        this.nombreGenero = nombreGenero;
    }

    public LocalDate getFechaAgregacion() {
        return fechaAgregacion;
    }

    public void setFechaAgregacion(LocalDate fechaAgregacion) {
        this.fechaAgregacion = fechaAgregacion;
    }
}