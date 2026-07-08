/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaces;

import DTO.ArtistaDTO;
import Excepciones.NegocioException;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author BALAMRUSH
 */
public interface IArtistaBO{
    List<ArtistaDTO> agregarMasivo(List<ArtistaDTO> artistasDTO) throws NegocioException;

    List<ArtistaDTO> consultarTodos() throws NegocioException;

    ArtistaDTO consultarPorId(ObjectId idArtista) throws NegocioException;

    List<ArtistaDTO> buscarPorNombre(String nombre) throws NegocioException;

    List<ArtistaDTO> buscarPorGenero(ObjectId idGenero) throws NegocioException;
    
}
