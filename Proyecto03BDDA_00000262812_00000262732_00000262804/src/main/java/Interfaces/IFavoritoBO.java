/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaces;

import DTO.FavoritoDTO;
import Excepciones.NegocioException;
import java.util.List;

/**
 *
 * @author Andre
 */
public interface IFavoritoBO {
    boolean agregarArtistaFavorito(String idUsuario, String idArtista) throws NegocioException;

    boolean agregarAlbumFavorito(String idUsuario, String idAlbum) throws NegocioException;

    boolean agregarCancionFavorita(String idUsuario, String idCancion) throws NegocioException;

    boolean eliminarArtistaFavorito(String idUsuario, String idArtista) throws NegocioException;

    boolean eliminarAlbumFavorito(String idUsuario, String idAlbum) throws NegocioException;

    boolean eliminarCancionFavorita(String idUsuario, String idCancion) throws NegocioException;

    boolean eliminarElementoFavorito(String idUsuario, String tipo, String idElemento) throws NegocioException;

    boolean eliminarFavoritosPorGenero(String idUsuario, String idGenero) throws NegocioException;

    List<FavoritoDTO> buscarFavoritos(String idUsuario, String texto, String tipo, String idGenero) throws NegocioException;
}
