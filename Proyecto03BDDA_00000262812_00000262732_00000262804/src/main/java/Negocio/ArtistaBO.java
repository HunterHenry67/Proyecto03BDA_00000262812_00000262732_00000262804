/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import DTO.AlbumDTO;
import DTO.ArtistaDTO;
import DTO.CancionDTO;
import DTO.IntegranteDTO;
import Entidades.Album;
import Entidades.Artista;
import Entidades.Cancion;
import Entidades.Integrante;
import Excepciones.NegocioException;
import Excepciones.PersistenciaException;
import Interfaces.IArtistaBO;
import Interfaces.IArtistaDAO;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author BALAMRUSH
 */
public class ArtistaBO implements IArtistaBO{
    private IArtistaDAO artistaDAO;

    public ArtistaBO(IArtistaDAO artistaDAO) {
        this.artistaDAO = artistaDAO;
    }

    @Override
    public List<ArtistaDTO> agregarMasivo(List<ArtistaDTO> artistasDTO) throws NegocioException {
        try{
            if (artistasDTO == null || artistasDTO.isEmpty()) {
                throw new NegocioException("La lista de artistas no puede estar vacía.");
            }
            List<Artista> artistas = new ArrayList<>();
            for (ArtistaDTO dto : artistasDTO) {
                validarArtista(dto);
                artistas.add(convertirAEntidad(dto));
            }
            List<Artista> artistasGuardados = artistaDAO.agregarMasivo(artistas);
            List<ArtistaDTO> resultado = new ArrayList<>();
            for (Artista artista : artistasGuardados) {
                resultado.add(convertirADTO(artista));
            }
            return resultado;
        }catch(PersistenciaException ex){
            throw new NegocioException("Error al agregar artistas masivos: "+ex.getMessage());
        }
    }

    @Override
    public List<ArtistaDTO> consultarTodos() throws NegocioException {
        try{
            List<Artista> artistas = artistaDAO.consultarTodos();
            List<ArtistaDTO> resultado = new ArrayList<>();
            for (Artista artista : artistas) {
                resultado.add(convertirADTO(artista));
            }
            return resultado;
        }catch(PersistenciaException ex){
            throw new NegocioException("Error al consultra todos los artistas.");
        }
    }

    @Override
    public ArtistaDTO consultarPorId(ObjectId idArtista) throws NegocioException {
         try {
            if (idArtista == null) {
                throw new NegocioException("El id del artista no puede ser nulo.");
            }
            Artista artista = artistaDAO.consultarPorId(idArtista);
            if (artista == null) {
                throw new NegocioException("Artista no encontrado.");
            }
            return convertirADTO(artista);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al consultar artista por id: " + ex.getMessage());
        }
    }

    @Override
    public List<ArtistaDTO> buscarPorNombre(String nombre) throws NegocioException {
        try {
            List<Artista> artistas;
            if (nombre == null || nombre.trim().isEmpty()) {
                artistas = artistaDAO.consultarTodos();
            } else {
                artistas = artistaDAO.buscarPorNombre(nombre.trim());
            }
            List<ArtistaDTO> resultado = new ArrayList<>();
            for (Artista artista : artistas) {
                resultado.add(convertirADTO(artista));
            }
            return resultado;
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al buscar artistas por nombre: " + ex.getMessage());
        }
    }

    @Override
    public List<ArtistaDTO> buscarPorGenero(ObjectId idGenero) throws NegocioException {
         try {
            if (idGenero == null) {
                return consultarTodos();
            }
            List<Artista> artistas = artistaDAO.buscarPorGenero(idGenero);
            List<ArtistaDTO> resultado = new ArrayList<>();
            for (Artista artista : artistas) {
                resultado.add(convertirADTO(artista));
            }
            return resultado;
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al buscar artistas por género: " + ex.getMessage());
        }
    }
    
    private void validarArtista(ArtistaDTO dto) throws NegocioException {
        if (dto == null) {
            throw new NegocioException("El artista no puede ser nulo.");
        }
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new NegocioException("Favor de poner un nombre al artista.");
        }
        if (dto.getImagen() == null || dto.getImagen().trim().isEmpty()) {
            throw new NegocioException("Favor de poner un nombre al artista.");
        }
        if (dto.getIdGenero() == null || dto.getIdGenero().trim().isEmpty()) {
            throw new NegocioException("Favor de poner un género al artista.");
        }
        if (dto.getIntegrantes() == null || dto.getIntegrantes().isEmpty()) {
            throw new NegocioException("El artista debe tener al menos un integrante.");
        }
        if (dto.getAlbumes() == null || dto.getAlbumes().isEmpty()) {
            throw new NegocioException("El artista debe tener al menos un álbum.");
        }
    }
  
}
