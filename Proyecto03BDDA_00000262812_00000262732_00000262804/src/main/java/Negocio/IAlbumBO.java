/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Negocio;

import DTO.AlbumDTO;
import DTO.CancionDTO;
import Entidades.Album;
import Excepciones.NegocioException;
import Excepciones.PersistenciaException;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author user
 */
public interface IAlbumBO {
    AlbumDTO consultarPorId(ObjectId idAlbum) throws NegocioException;
    List<AlbumDTO> consultarPorArtista(String idArtista) throws NegocioException;
    List<AlbumDTO> consultarTodos() throws NegocioException;

}
