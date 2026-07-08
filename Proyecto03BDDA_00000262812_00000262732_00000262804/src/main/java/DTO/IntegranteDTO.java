/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import java.time.LocalDate;
import org.bson.types.ObjectId;

/**
 *
 * @author BALAMRUSH
 */
public class IntegranteDTO {
    private ObjectId idPersona;
    private String rol;
    private LocalDate fechaIngreso;
    private LocalDate fechaSalida;
    private boolean activo;

    public IntegranteDTO() {
    }

    public IntegranteDTO(ObjectId idPersona, String rol, LocalDate fechaIngreso, LocalDate fechaSalida,boolean activo) {
        this.idPersona = idPersona;
        this.rol = rol;
        this.fechaIngreso = fechaIngreso;
        this.fechaSalida = fechaSalida;
        this.activo = activo;
    }

    public ObjectId getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(ObjectId idPersona) {
        this.idPersona = idPersona;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
