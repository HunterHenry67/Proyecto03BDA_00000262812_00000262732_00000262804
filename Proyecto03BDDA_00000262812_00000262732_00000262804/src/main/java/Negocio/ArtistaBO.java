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
import Persistencia.ArtistaDAO;
import Persistencia.ConexionBD;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author BALAMRUSH
 */
public class ArtistaBO implements IArtistaBO {

    private final IArtistaDAO artistaDAO;

    public ArtistaBO() {
        this(new ArtistaDAO(new ConexionBD()));
    }

    public ArtistaBO(IArtistaDAO artistaDAO) {
        if (artistaDAO == null) {
            throw new IllegalArgumentException(
                    "El DAO de artista no puede ser nulo."
            );
        }

        this.artistaDAO = artistaDAO;
    }

    @Override
    public List<ArtistaDTO> agregarMasivo(
            List<ArtistaDTO> artistasDTO
    ) throws NegocioException {

        if (artistasDTO == null || artistasDTO.isEmpty()) {
            throw new NegocioException(
                    "La lista de artistas no puede estar vacía."
            );
        }

        try {
            List<Artista> artistas = new ArrayList<>();

            for (ArtistaDTO dto : artistasDTO) {
                validarArtista(dto);
                artistas.add(convertirAEntidad(dto));
            }

            List<Artista> artistasGuardados =
                    artistaDAO.agregarMasivo(artistas);

            List<ArtistaDTO> resultado = new ArrayList<>();

            if (artistasGuardados != null) {
                for (Artista artista : artistasGuardados) {
                    resultado.add(convertirADTO(artista));
                }
            }

            return resultado;

        } catch (PersistenciaException ex) {
            throw new NegocioException(
                    "Error al agregar artistas masivos: "
                    + ex.getMessage()
            );

        } catch (IllegalArgumentException | DateTimeParseException ex) {
            throw new NegocioException(
                    "Los datos de uno de los artistas no son válidos: "
                    + ex.getMessage()
            );
        }
    }

    @Override
    public List<ArtistaDTO> consultarTodos() throws NegocioException {
        try {
            List<Artista> artistas = artistaDAO.consultarTodos();
            List<ArtistaDTO> resultado = new ArrayList<>();

            if (artistas != null) {
                for (Artista artista : artistas) {
                    resultado.add(convertirADTO(artista));
                }
            }

            return resultado;

        } catch (PersistenciaException ex) {
            throw new NegocioException(
                    "Error al consultar todos los artistas: "
                    + ex.getMessage()
            );
        }
    }

    @Override
    public ArtistaDTO consultarPorId(
            ObjectId idArtista
    ) throws NegocioException {

        if (idArtista == null) {
            throw new NegocioException(
                    "El id del artista no puede ser nulo."
            );
        }

        try {
            Artista artista = artistaDAO.consultarPorId(idArtista);

            if (artista == null) {
                throw new NegocioException(
                        "Artista no encontrado."
                );
            }

            return convertirADTO(artista);

        } catch (PersistenciaException ex) {
            throw new NegocioException(
                    "Error al consultar artista por id: "
                    + ex.getMessage()
            );
        }
    }

    @Override
    public List<ArtistaDTO> buscarPorNombre(
            String nombre
    ) throws NegocioException {

        try {
            List<Artista> artistas;

            if (nombre == null || nombre.trim().isEmpty()) {
                artistas = artistaDAO.consultarTodos();
            } else {
                artistas = artistaDAO.buscarPorNombre(
                        nombre.trim()
                );
            }

            List<ArtistaDTO> resultado = new ArrayList<>();

            if (artistas != null) {
                for (Artista artista : artistas) {
                    resultado.add(convertirADTO(artista));
                }
            }

            return resultado;

        } catch (PersistenciaException ex) {
            throw new NegocioException(
                    "Error al buscar artistas por nombre: "
                    + ex.getMessage()
            );
        }
    }

    @Override
    public List<ArtistaDTO> buscarPorGenero(
            ObjectId idGenero
    ) throws NegocioException {

        if (idGenero == null) {
            return consultarTodos();
        }

        try {
            List<Artista> artistas =
                    artistaDAO.buscarPorGenero(idGenero);

            List<ArtistaDTO> resultado = new ArrayList<>();

            if (artistas != null) {
                for (Artista artista : artistas) {
                    resultado.add(convertirADTO(artista));
                }
            }

            return resultado;

        } catch (PersistenciaException ex) {
            throw new NegocioException(
                    "Error al buscar artistas por género: "
                    + ex.getMessage()
            );
        }
    }

    private void validarArtista(
            ArtistaDTO dto
    ) throws NegocioException {

        if (dto == null) {
            throw new NegocioException(
                    "El artista no puede ser nulo."
            );
        }

        if (dto.getNombre() == null
                || dto.getNombre().trim().isEmpty()) {

            throw new NegocioException(
                    "Favor de ingresar un nombre para el artista."
            );
        }

        if (dto.getImagen() == null
                || dto.getImagen().trim().isEmpty()) {

            throw new NegocioException(
                    "Favor de ingresar una imagen para el artista."
            );
        }

        if (dto.getIdGenero() == null
                || dto.getIdGenero().trim().isEmpty()) {

            throw new NegocioException(
                    "Favor de seleccionar un género para el artista."
            );
        }

        if (!ObjectId.isValid(dto.getIdGenero())) {
            throw new NegocioException(
                    "El id del género del artista no es válido."
            );
        }

        if (dto.getIntegrantes() == null
                || dto.getIntegrantes().isEmpty()) {

            throw new NegocioException(
                    "El artista debe tener al menos un integrante."
            );
        }

        if (dto.getAlbumes() == null
                || dto.getAlbumes().isEmpty()) {

            throw new NegocioException(
                    "El artista debe tener al menos un álbum."
            );
        }
    }

    public static Artista convertirAEntidad(
            ArtistaDTO dto
    ) {

        if (dto == null) {
            return null;
        }

        Artista artista = new Artista();

        if (dto.getId() != null
                && !dto.getId().isBlank()
                && ObjectId.isValid(dto.getId())) {

            artista.setId(new ObjectId(dto.getId()));

        } else {
            artista.setId(new ObjectId());
        }

        artista.setNombre(dto.getNombre());
        artista.setImagen(dto.getImagen());

        if (dto.getIdGenero() != null
                && ObjectId.isValid(dto.getIdGenero())) {

            artista.setIdGenero(
                    new ObjectId(dto.getIdGenero())
            );
        }

        List<Integrante> integrantes =
                new ArrayList<>();

        if (dto.getIntegrantes() != null) {
            for (IntegranteDTO integranteDTO
                    : dto.getIntegrantes()) {

                if (integranteDTO == null) {
                    continue;
                }

                Integrante integrante =
                        new Integrante();

                if (integranteDTO.getIdPersona() != null
                        && ObjectId.isValid(
                                integranteDTO.getIdPersona())) {

                    integrante.setIdPersona(
                            new ObjectId(
                                    integranteDTO.getIdPersona()
                            )
                    );
                }

                integrante.setRol(
                        integranteDTO.getRol()
                );

                integrante.setFechaIngreso(
                        integranteDTO.getFechaIngreso()
                );

                integrante.setFechaSalida(
                        integranteDTO.getFechaSalida()
                );

                integrante.setActivo(
                        integranteDTO.isActivo()
                );

                integrantes.add(integrante);
            }
        }

        artista.setIntegrantes(integrantes);

        List<Album> albumes = new ArrayList<>();

        if (dto.getAlbumes() != null) {
            for (AlbumDTO albumDTO : dto.getAlbumes()) {

                if (albumDTO == null) {
                    continue;
                }

                Album album = new Album();

                if (albumDTO.getIdAlbum() != null
                        && !albumDTO.getIdAlbum().isBlank()
                        && ObjectId.isValid(
                                albumDTO.getIdAlbum())) {

                    album.setId(
                            new ObjectId(
                                    albumDTO.getIdAlbum()
                            )
                    );

                } else {
                    album.setId(new ObjectId());
                }

                album.setNombre(
                        albumDTO.getNombre()
                );

                /*
                 * Corrección:
                 * Solo convierte la fecha si no es null
                 * y tampoco está vacía.
                 */
                if (albumDTO.getFechaLanzamiento() != null
                        && !albumDTO
                                .getFechaLanzamiento()
                                .isBlank()) {

                    album.setFechaLanzamiento(
                            LocalDate.parse(
                                    albumDTO
                                            .getFechaLanzamiento()
                                            .trim()
                            )
                    );

                } else {
                    album.setFechaLanzamiento(null);
                }

                if (albumDTO.getIdGenero() != null
                        && ObjectId.isValid(
                                albumDTO.getIdGenero())) {

                    album.setIdGenero(
                            new ObjectId(
                                    albumDTO.getIdGenero()
                            )
                    );
                }

                album.setImagenPortada(
                        albumDTO.getImagenPortada()
                );

                List<Cancion> canciones =
                        new ArrayList<>();

                if (albumDTO.getCanciones() != null) {
                    for (CancionDTO cancionDTO
                            : albumDTO.getCanciones()) {

                        if (cancionDTO == null) {
                            continue;
                        }

                        Cancion cancion =
                                new Cancion();

                        if (cancionDTO.getIdCancion() != null
                                && !cancionDTO
                                        .getIdCancion()
                                        .isBlank()
                                && ObjectId.isValid(
                                        cancionDTO
                                                .getIdCancion())) {

                            cancion.setId(
                                    new ObjectId(
                                            cancionDTO
                                                    .getIdCancion()
                                    )
                            );

                        } else {
                            cancion.setId(
                                    new ObjectId()
                            );
                        }

                        cancion.setNombre(
                                cancionDTO.getNombre()
                        );

                        cancion.setDuracion(
                                cancionDTO.getDuracion()
                        );

                        if (cancionDTO.getIdGenero() != null
                                && ObjectId.isValid(
                                        cancionDTO
                                                .getIdGenero())) {

                            cancion.setIdGenero(
                                    new ObjectId(
                                            cancionDTO
                                                    .getIdGenero()
                                    )
                            );
                        }

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

    public static ArtistaDTO convertirADTO(
            Artista artista
    ) {

        if (artista == null) {
            return null;
        }

        ArtistaDTO dto = new ArtistaDTO();

        if (artista.getId() != null) {
            dto.setId(
                    artista.getId().toHexString()
            );
        }

        dto.setNombre(
                artista.getNombre()
        );

        dto.setImagen(
                artista.getImagen()
        );

        if (artista.getIdGenero() != null) {
            dto.setIdGenero(
                    artista.getIdGenero().toHexString()
            );
        }

        List<IntegranteDTO> integrantesDTO =
                new ArrayList<>();

        if (artista.getIntegrantes() != null) {
            for (Integrante integrante
                    : artista.getIntegrantes()) {

                if (integrante == null) {
                    continue;
                }

                IntegranteDTO integranteDTO =
                        new IntegranteDTO();

                if (integrante.getIdPersona() != null) {
                    integranteDTO.setIdPersona(
                            integrante
                                    .getIdPersona()
                                    .toHexString()
                    );
                }

                integranteDTO.setRol(
                        integrante.getRol()
                );

                integranteDTO.setFechaIngreso(
                        integrante.getFechaIngreso()
                );

                integranteDTO.setFechaSalida(
                        integrante.getFechaSalida()
                );

                integranteDTO.setActivo(
                        integrante.isActivo()
                );

                integrantesDTO.add(integranteDTO);
            }
        }

        dto.setIntegrantes(integrantesDTO);

        List<AlbumDTO> albumesDTO =
                new ArrayList<>();

        if (artista.getAlbumes() != null) {
            for (Album album
                    : artista.getAlbumes()) {

                if (album == null) {
                    continue;
                }

                AlbumDTO albumDTO =
                        new AlbumDTO();

                if (album.getId() != null) {
                    albumDTO.setIdAlbum(
                            album.getId().toHexString()
                    );
                }

                albumDTO.setNombre(
                        album.getNombre()
                );

                if (album.getFechaLanzamiento() != null) {
                    albumDTO.setFechaLanzamiento(
                            album
                                    .getFechaLanzamiento()
                                    .toString()
                    );
                } else {
                    albumDTO.setFechaLanzamiento("");
                }

                if (album.getIdGenero() != null) {
                    albumDTO.setIdGenero(
                            album
                                    .getIdGenero()
                                    .toHexString()
                    );
                }

                albumDTO.setImagenPortada(
                        album.getImagenPortada()
                );

                List<CancionDTO> cancionesDTO =
                        new ArrayList<>();

                if (album.getCanciones() != null) {
                    for (Cancion cancion
                            : album.getCanciones()) {

                        if (cancion == null) {
                            continue;
                        }

                        CancionDTO cancionDTO =
                                new CancionDTO();

                        if (cancion.getId() != null) {
                            cancionDTO.setIdCancion(
                                    cancion
                                            .getId()
                                            .toHexString()
                            );
                        }

                        cancionDTO.setNombre(
                                cancion.getNombre()
                        );

                        cancionDTO.setDuracion(
                                cancion.getDuracion()
                        );

                        if (cancion.getIdGenero() != null) {
                            cancionDTO.setIdGenero(
                                    cancion
                                            .getIdGenero()
                                            .toHexString()
                            );
                        }

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