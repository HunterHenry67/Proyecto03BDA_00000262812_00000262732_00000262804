/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaces;

import Entidades.Persona;
import Excepciones.PersistenciaException;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author BALAMRUSH
 */
public interface IPersonaDAO {
    List<Persona> agregarMasivo(List<Persona> personas) throws PersistenciaException;

    Persona consultarPorId(ObjectId idPersona) throws PersistenciaException;
}
