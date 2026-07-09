/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaces;

import DTO.GeneroNoDeseadoDTO;
import DTO.UsuarioDTO;
import Excepciones.NegocioException;
import java.util.List;

/**
 *
 * @author Andre
 */
public interface IUsuarioBO {

    UsuarioDTO registrar(UsuarioDTO usuarioDTO) throws NegocioException;

    UsuarioDTO iniciarSesion(String usuarioOCorreo, String contrasena) throws NegocioException;

    boolean actualizarPerfil(UsuarioDTO usuarioDTO) throws NegocioException;

    boolean agregarGeneroNoDeseado(String idUsuario, GeneroNoDeseadoDTO generoNoDeseadoDTO) throws NegocioException;

    boolean eliminarGeneroNoDeseado(String idUsuario, String idGeneroNoDeseado) throws NegocioException;

    List<GeneroNoDeseadoDTO> consultarGenerosNoDeseados(String idUsuario) throws NegocioException;
}