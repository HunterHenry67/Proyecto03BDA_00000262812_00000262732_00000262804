/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Persistencia;

import Entidades.Album;
import Entidades.Cancion;
import Excepciones.PersistenciaException;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author user
 */
public interface IAlbumDAO {
    
    Album agregar(Album album) throws PersistenciaException;


    Album consultarPorId(ObjectId idAlbum) throws PersistenciaException;

    List<Album> consultarPorArtista(ObjectId idArtista) throws PersistenciaException;

    List<Album> consultarTodos() throws PersistenciaException;

}
