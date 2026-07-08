/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaces;

import Entidades.Favorito;
import Excepciones.PersistenciaException;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author Andre
 */

public interface IFavoritoDAO {

    Favorito consultarPorUsuario(ObjectId idUsuario) throws PersistenciaException;

    boolean agregarArtista(ObjectId idUsuario, ObjectId idArtista) throws PersistenciaException;

    boolean agregarAlbum(ObjectId idUsuario, ObjectId idAlbum) throws PersistenciaException;

    boolean agregarCancion(ObjectId idUsuario, ObjectId idCancion) throws PersistenciaException;

    boolean eliminarArtista(ObjectId idUsuario, ObjectId idArtista) throws PersistenciaException;

    boolean eliminarAlbum(ObjectId idUsuario, ObjectId idAlbum) throws PersistenciaException;

    boolean eliminarCancion(ObjectId idUsuario, ObjectId idCancion) throws PersistenciaException;

    boolean eliminarElemento(ObjectId idUsuario, String tipo, ObjectId idElemento) throws PersistenciaException;

    boolean eliminarFavoritosPorGenero(ObjectId idUsuario, ObjectId idGenero) throws PersistenciaException;

    List<Document> buscarFavoritos(ObjectId idUsuario, String texto, String tipo, ObjectId idGenero) throws PersistenciaException;
}