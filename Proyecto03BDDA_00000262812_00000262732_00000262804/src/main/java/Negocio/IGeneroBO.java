/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Negocio;

import DTO.GeneroDTO;
import Excepciones.PersistenciaException;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author user
 */
public interface IGeneroBO {
    GeneroDTO consultarPorId(ObjectId idGenero) throws PersistenciaException;

    GeneroDTO consultarPorNombre(String nombre) throws PersistenciaException;

    List<GeneroDTO> consultarTodos() throws PersistenciaException;
}
