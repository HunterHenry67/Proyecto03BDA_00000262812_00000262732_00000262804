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
public class Usuario {
    private ObjectId id;
    private String nombreUsuario;
    private String correo;
    private String contrasena;
    private String imagenPerfil;
    private List<GeneroNoDeseado> generoNoDeseado;

    public Usuario() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public List<GeneroNoDeseado> getGeneroNoDeseado() {
        return generoNoDeseado;
    }

    public void setGeneroNoDeseado(List<GeneroNoDeseado> generoNoDeseado) {
        this.generoNoDeseado = generoNoDeseado;
    }
  
}
