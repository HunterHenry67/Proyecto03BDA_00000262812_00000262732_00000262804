/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Interfaces.IGeneroBO;
import DTO.GeneroDTO;
import Entidades.Genero;
import Excepciones.PersistenciaException;
import Persistencia.GeneroDAO;
import Interfaces.IGeneroDAO;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author user
 */
public class GeneroBO implements IGeneroBO{

    private final IGeneroDAO generoDAO;
    
    public GeneroBO() {
        this.generoDAO = new GeneroDAO();
    }

    @Override
    public GeneroDTO consultarPorId(ObjectId idGenero) throws PersistenciaException {
        if (idGenero == null) {
            throw new IllegalArgumentException("El ID del género debe ser válido");
        }
        Genero genero = generoDAO.consultarPorId(idGenero);
        
        if (genero == null) {
            throw new IllegalArgumentException("El género buscado no existe");
        }
        
        return mapearADTO(genero);
    }

    @Override
    public GeneroDTO consultarPorNombre(String nombre) throws PersistenciaException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Ingrese nombre correcto o válido");
        }
        Genero genero = generoDAO.consultarPorNombre(nombre);
        
        if (genero == null) {
            throw new IllegalArgumentException("Género no encontrado");
        }
        
        return mapearADTO(genero);
    }

    @Override
    public List<GeneroDTO> consultarTodos() throws PersistenciaException {
        List<Genero> generos = generoDAO.consultarTodos();
        List<GeneroDTO> generosDTO = new ArrayList<>();

        if (generos != null && !generos.isEmpty()) {
            for (Genero genero : generos) {
                generosDTO.add(mapearADTO(genero));
            }
        }

        return generosDTO;
    }

    private GeneroDTO mapearADTO(Genero genero) {
        GeneroDTO dto = new GeneroDTO();
        
        if (genero.getId() != null) {
            dto.setId(genero.getId().toHexString()); 
        }
        
        dto.setNombre(genero.getNombre());
        
        return dto;
    }

   
    
}
