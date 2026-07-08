/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Negocio;
import DTO.GeneroNoDeseadoDTO;
import Excepciones.NegocioException;
import java.util.List;
/**
 *
 * @author Andre
 */
public interface IGeneroNoDeseadoBO {
    boolean agregarGenero(String idUsuario, String idGenero) throws NegocioException;
    boolean eliminarGenero(String idUsuario, String idGenero) throws NegocioException;
    List<GeneroNoDeseadoDTO> consultar(String idUsuario) throws NegocioException;
}
