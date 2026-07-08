/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaces;

import DTO.PersonaDTO;
import Excepciones.NegocioException;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author BALAMRUSH
 */
public interface IPersonaBO {
    List<PersonaDTO> agregarMasivo(List<PersonaDTO> personas) throws NegocioException;

    PersonaDTO consultarPorId(ObjectId idPersona) throws NegocioException;
}
