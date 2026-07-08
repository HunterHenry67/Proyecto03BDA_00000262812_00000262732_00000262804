/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import DTO.PersonaDTO;
import Entidades.Persona;
import Excepciones.NegocioException;
import Excepciones.PersistenciaException;
import Interfaces.IPersonaBO;
import Interfaces.IPersonaDAO;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author BALAMRUSH
 */
public class PersonaBO implements IPersonaBO {

    private IPersonaDAO personaDAO;

    public PersonaBO(IPersonaDAO personaDAO) {
        this.personaDAO = personaDAO;
    }

    @Override
    public List<PersonaDTO> agregarMasivo(List<PersonaDTO> personasDTO) throws NegocioException {
        try {
            if (personasDTO == null || personasDTO.isEmpty()) {
                throw new NegocioException("La lista de personas no puede estar vacía.");
            }
            List<Persona> personas = new ArrayList<>();
            for (PersonaDTO dto : personasDTO) {
                personas.add(convertirAEntidad(dto));
            }
            List<Persona> personasGuardadas = personaDAO.agregarMasivo(personas);
            List<PersonaDTO> resultado = new ArrayList<>();
            for (Persona persona : personasGuardadas) {
                resultado.add(convertirADTO(persona));
            }
            return resultado;
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al agregar masivamente las personas: " + ex.getMessage());
        }
    }

    @Override
    public PersonaDTO consultarPorId(ObjectId idPersona) throws NegocioException {
        try {
            if (idPersona == null) {
                throw new NegocioException("El id de la persona no puede ser nulo.");
            }
            Persona persona = personaDAO.consultarPorId(idPersona);
            if (persona == null) {
                throw new NegocioException("No se encontró la persona.");
            }
            return convertirADTO(persona);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al consultar una persona por id: "+ex.getMessage());
        }
    }

    private Persona convertirAEntidad(PersonaDTO dto) {
        Persona persona = new Persona();
        if (dto.getId() != null && !dto.getId().isEmpty()) {
            persona.setId(new ObjectId(dto.getId()));
        } else {
            persona.setId(new ObjectId());
        }
        persona.setNombre(dto.getNombre());
        persona.setDescripcion(dto.getDescripcion());
        return persona;
    }

    private PersonaDTO convertirADTO(Persona persona) {
        PersonaDTO dto = new PersonaDTO();
        dto.setId(persona.getId().toHexString());
        dto.setNombre(persona.getNombre());
        dto.setDescripcion(persona.getDescripcion());
        return dto;
    }

}
