/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaces;

import Entidades.Genero;
import Excepciones.PersistenciaException;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author user
 */
public interface IGeneroDAO {
    Genero agregar(Genero genero) throws PersistenciaException;
    
    Genero consultarPorId(ObjectId idGenero) throws PersistenciaException;

    Genero consultarPorNombre(String nombre) throws PersistenciaException;

    List<Genero> consultarTodos() throws PersistenciaException;

}
