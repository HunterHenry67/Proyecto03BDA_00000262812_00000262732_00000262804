/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaces;

import Entidades.Artista;
import Excepciones.PersistenciaException;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author BALAMRUSH
 */
public interface IArtistaDAO {
    List<Artista> agregarMasivo(List<Artista> artistas) throws PersistenciaException;

    List<Artista> consultarTodos() throws PersistenciaException;

    Artista consultarPorId(ObjectId idArtista) throws PersistenciaException;

    List<Artista> buscarPorNombre(String nombre) throws PersistenciaException;

    List<Artista> buscarPorGenero(ObjectId idGenero) throws PersistenciaException;
}
