/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaces;

import Entidades.GeneroNoDeseado;
import Entidades.Usuario;
import Excepciones.PersistenciaException;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author user
 */
public interface IUsuarioDAO {
    Usuario agregar(Usuario usuario) throws PersistenciaException;
    Usuario consultarPorId(ObjectId idUsuario) throws PersistenciaException;
    Usuario consultarPorNombreUsuario(String nombreUsuario) throws PersistenciaException;
    Usuario consultarPorCorreo(String correo) throws PersistenciaException;
    boolean existeNombreUsuario(String nombreUsuario) throws PersistenciaException;
    boolean existeCorreo(String correo) throws PersistenciaException;
    boolean actualizarPerfil(Usuario usuario) throws PersistenciaException;
    boolean agregarGeneroNoDeseado(ObjectId idUsuario, GeneroNoDeseado generoNoDeseado) throws PersistenciaException;
    boolean eliminarGeneroNoDeseado(ObjectId idUsuario, ObjectId idGeneroNoDeseado) throws PersistenciaException;
    List<GeneroNoDeseado> consultarGenerosNoDeseados(ObjectId idUsuario) throws PersistenciaException;

    boolean tieneGeneroNoDeseado(ObjectId idUsuario, ObjectId idGeneroNoDeseado) throws PersistenciaException;

}
