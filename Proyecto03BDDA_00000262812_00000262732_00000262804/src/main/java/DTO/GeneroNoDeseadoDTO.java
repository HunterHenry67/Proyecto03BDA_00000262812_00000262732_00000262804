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

    private String idGND;
    private LocalDate fechaAgregacion;

    public GeneroNoDeseadoDTO() {
    }

    public GeneroNoDeseadoDTO(String idGND, LocalDate fechaAgregacion) {
        this.idGND = idGND;
        this.fechaAgregacion = fechaAgregacion;
    }

    public String getIdGND() {
        return idGND;
    }

    public void setIdGND(String idGND) {
        this.idGND = idGND;
    }

    public LocalDate getFechaAgregacion() {
        return fechaAgregacion;
    }

    public void setFechaAgregacion(LocalDate fechaAgregacion) {
        this.fechaAgregacion = fechaAgregacion;
    }
}