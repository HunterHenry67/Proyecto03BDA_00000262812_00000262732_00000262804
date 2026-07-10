/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilerias;

import DTO.UsuarioDTO;

/**
 *
 * @author BALAMRUSH
 */
public class Sesion {
     private static UsuarioDTO usuarioActual;

    private Sesion() {
    }

    public static void iniciarSesion(UsuarioDTO usuario) {
        usuarioActual = usuario;
    }

    public static UsuarioDTO getUsuarioActual() {
        return usuarioActual;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }

    public static boolean haySesion() {
        return usuarioActual != null;
    }
}
