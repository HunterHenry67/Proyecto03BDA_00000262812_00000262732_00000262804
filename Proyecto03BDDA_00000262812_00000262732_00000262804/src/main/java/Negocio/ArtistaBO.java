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
import java.time.LocalDate;
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
    
    public static Artista convertirAEntidad(ArtistaDTO dto) {
        Artista artista = new Artista();
        if (dto.getId() != null && !dto.getId().isEmpty()) {
            artista.setId(new ObjectId(dto.getId()));
        } else {
            artista.setId(new ObjectId());
        }
        artista.setNombre(dto.getNombre());
        artista.setImagen(dto.getImagen());
        artista.setIdGenero(new ObjectId(dto.getIdGenero()));
        List<Integrante> integrantes = new ArrayList<>();
        if (dto.getIntegrantes() != null) {
            for (IntegranteDTO integranteDTO : dto.getIntegrantes()) {
                Integrante integrante = new Integrante();
                integrante.setIdPersona(new ObjectId(integranteDTO.getIdPersona()));
                integrante.setRol(integranteDTO.getRol());
                integrante.setFechaIngreso(integranteDTO.getFechaIngreso());
                integrante.setFechaSalida(integranteDTO.getFechaSalida());
                integrante.setActivo(integranteDTO.isActivo());
                integrantes.add(integrante);
            }
        }

        artista.setIntegrantes(integrantes);

        List<Album> albumes = new ArrayList<>();

        if (dto.getAlbumes() != null) {
            for (AlbumDTO albumDTO : dto.getAlbumes()) {
                Album album = new Album();

                if (albumDTO.getIdAlbum() != null && !albumDTO.getIdAlbum().isEmpty()) {
                    album.setId(new ObjectId(albumDTO.getIdAlbum()));
                } else {
                    album.setId(new ObjectId());
                }
                album.setNombre(albumDTO.getNombre());
                album.setFechaLanzamiento(LocalDate.parse(albumDTO.getFechaLanzamiento()));
                album.setIdGenero(new ObjectId(albumDTO.getIdGenero()));
                album.setImagenPortada(albumDTO.getImagenPortada());
                List<Cancion> canciones = new ArrayList<>();
                if (albumDTO.getCanciones() != null) {
                    for (CancionDTO cancionDTO : albumDTO.getCanciones()) {
                        Cancion cancion = new Cancion();
                        if (cancionDTO.getIdCancion() != null && !cancionDTO.getIdCancion().isEmpty()) {
                            cancion.setId(new ObjectId(cancionDTO.getIdCancion()));
                        } else {
                            cancion.setId(new ObjectId());
                        }
                        cancion.setNombre(cancionDTO.getNombre());
                        cancion.setDuracion(cancionDTO.getDuracion());
                        cancion.setIdGenero(new ObjectId(cancionDTO.getIdGenero()));
                        canciones.add(cancion);
                    }
                }
                album.setCanciones(canciones);
                albumes.add(album);
            }
        }
        artista.setAlbumes(albumes);
        return artista;
    }

    public static ArtistaDTO convertirADTO(Artista artista) {
        ArtistaDTO dto = new ArtistaDTO();
        dto.setId(artista.getId().toHexString());
        dto.setNombre(artista.getNombre());
        dto.setImagen(artista.getImagen());
        dto.setIdGenero(artista.getIdGenero().toHexString());
        List<IntegranteDTO> integrantesDTO = new ArrayList<>();
        if (artista.getIntegrantes() != null) {
            for (Integrante integrante : artista.getIntegrantes()) {
                IntegranteDTO integranteDTO = new IntegranteDTO();
                integranteDTO.setIdPersona(integrante.getIdPersona().toHexString());
                integranteDTO.setRol(integrante.getRol());
                integranteDTO.setFechaIngreso(integrante.getFechaIngreso());
                integranteDTO.setFechaSalida(integrante.getFechaSalida());
                integranteDTO.setActivo(integrante.isActivo());
                integrantesDTO.add(integranteDTO);
            }
        }
        dto.setIntegrantes(integrantesDTO);
        List<AlbumDTO> albumesDTO = new ArrayList<>();
        if (artista.getAlbumes() != null) {
            for (Album album : artista.getAlbumes()) {
                AlbumDTO albumDTO = new AlbumDTO();
                albumDTO.setIdAlbum(album.getId().toHexString());
                albumDTO.setNombre(album.getNombre());
                albumDTO.setFechaLanzamiento(album.getFechaLanzamiento().toString());
                albumDTO.setIdGenero(album.getIdGenero().toHexString());
                albumDTO.setImagenPortada(album.getImagenPortada());
                List<CancionDTO> cancionesDTO = new ArrayList<>();
                if (album.getCanciones() != null) {
                    for (Cancion cancion : album.getCanciones()) {
                        CancionDTO cancionDTO = new CancionDTO();
                        cancionDTO.setIdCancion(cancion.getId().toHexString());
                        cancionDTO.setNombre(cancion.getNombre());
                        cancionDTO.setDuracion(cancion.getDuracion());
                        cancionDTO.setIdGenero(cancion.getIdGenero().toHexString());
                        cancionesDTO.add(cancionDTO);
                    }
                }
                albumDTO.setCanciones(cancionesDTO);
                albumesDTO.add(albumDTO);
            }
        }
        dto.setAlbumes(albumesDTO);
        return dto;
    }
  
}
