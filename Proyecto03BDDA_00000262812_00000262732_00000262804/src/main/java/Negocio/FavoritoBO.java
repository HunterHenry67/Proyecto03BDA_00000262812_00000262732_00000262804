/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import DTO.FavoritoDTO;
import Excepciones.NegocioException;
import Excepciones.PersistenciaException;
import Interfaces.IFavoritoBO;
import Interfaces.IFavoritoDAO;
import Persistencia.FavoritoDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;
import Interfaces.IUsuarioDAO;
import Persistencia.UsuarioDAO;

/**
 *
 * @author Andre
 */
public class FavoritoBO implements IFavoritoBO {

    private final IFavoritoDAO favoritoDAO;
    private final IUsuarioDAO usuarioDAO;

    public FavoritoBO() {
        this.favoritoDAO = new FavoritoDAO();
        this.usuarioDAO = new UsuarioDAO();
    }

    public FavoritoBO(
            IFavoritoDAO favoritoDAO
    ) {
        this.favoritoDAO = favoritoDAO;
        this.usuarioDAO = new UsuarioDAO();
    }

    public FavoritoBO(
            IFavoritoDAO favoritoDAO,
            IUsuarioDAO usuarioDAO
    ) {
        this.favoritoDAO = favoritoDAO;
        this.usuarioDAO = usuarioDAO;
    }

    @Override
public boolean agregarAlbumFavorito(
        String idUsuario,
        String idAlbum
) throws NegocioException {

    ObjectId usuario =
            convertirObjectId(
                    idUsuario,
                    "usuario"
            );

    ObjectId album =
            convertirObjectId(
                    idAlbum,
                    "álbum"
            );

    try {
        validarGeneroPermitido(
                usuario,
                "ALBUM",
                album
        );

        return favoritoDAO.agregarAlbum(
                usuario,
                album
        );

    } catch (PersistenciaException ex) {
        throw new NegocioException(
                ex.getMessage()
        );
    }
}

    @Override
public boolean agregarCancionFavorita(
        String idUsuario,
        String idCancion
) throws NegocioException {

    ObjectId usuario =
            convertirObjectId(
                    idUsuario,
                    "usuario"
            );

    ObjectId cancion =
            convertirObjectId(
                    idCancion,
                    "canción"
            );

    try {
        validarGeneroPermitido(
                usuario,
                "CANCION",
                cancion
        );

        return favoritoDAO.agregarCancion(
                usuario,
                cancion
        );

    } catch (PersistenciaException ex) {
        throw new NegocioException(
                ex.getMessage()
        );
    }
}

    @Override
    public boolean eliminarArtistaFavorito(String idUsuario, String idArtista) throws NegocioException {
        ObjectId usuario = convertirObjectId(idUsuario, "usuario");
        ObjectId artista = convertirObjectId(idArtista, "artista");

        try {
            return favoritoDAO.eliminarArtista(usuario, artista);
        } catch (PersistenciaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @Override
    public boolean eliminarAlbumFavorito(String idUsuario, String idAlbum) throws NegocioException {
        ObjectId usuario = convertirObjectId(idUsuario, "usuario");
        ObjectId album = convertirObjectId(idAlbum, "álbum");

        try {
            return favoritoDAO.eliminarAlbum(usuario, album);
        } catch (PersistenciaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @Override
    public boolean eliminarCancionFavorita(String idUsuario, String idCancion) throws NegocioException {
        ObjectId usuario = convertirObjectId(idUsuario, "usuario");
        ObjectId cancion = convertirObjectId(idCancion, "canción");

        try {
            return favoritoDAO.eliminarCancion(usuario, cancion);
        } catch (PersistenciaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @Override
    public boolean eliminarElementoFavorito(String idUsuario, String tipo, String idElemento) throws NegocioException {
        ObjectId usuario = convertirObjectId(idUsuario, "usuario");
        ObjectId elemento = convertirObjectId(idElemento, "elemento");
        String tipoNormalizado = normalizarTipoObligatorio(tipo);

        try {
            return favoritoDAO.eliminarElemento(usuario, tipoNormalizado, elemento);
        } catch (PersistenciaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @Override
    public boolean eliminarFavoritosPorGenero(String idUsuario, String idGenero) throws NegocioException {
        ObjectId usuario = convertirObjectId(idUsuario, "usuario");
        ObjectId genero = convertirObjectId(idGenero, "género");

        try {
            return favoritoDAO.eliminarFavoritosPorGenero(usuario, genero);
        } catch (PersistenciaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @Override
    public List<FavoritoDTO> buscarFavoritos(String idUsuario, String texto, String tipo, String idGenero) throws NegocioException {
        ObjectId usuario = convertirObjectId(idUsuario, "usuario");
        ObjectId genero = null;

        if (idGenero != null && !idGenero.isBlank()) {
            genero = convertirObjectId(idGenero, "género");
        }

        String tipoNormalizado = normalizarTipoBusqueda(tipo);

        try {
            List<Document> documentos = favoritoDAO.buscarFavoritos(usuario, texto, tipoNormalizado, genero);
            List<FavoritoDTO> favoritos = new ArrayList<>();

            for (Document documento : documentos) {
                favoritos.add(mapearADTO(documento));
            }

            return favoritos;
        } catch (PersistenciaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    private FavoritoDTO mapearADTO(Document documento) {
        FavoritoDTO dto = new FavoritoDTO();

        dto.setTipo(documento.getString("tipo"));
        dto.setIdElemento(documento.getString("idElemento"));
        dto.setIdArtista(documento.getString("idArtista"));
        dto.setIdAlbum(documento.getString("idAlbum"));
        dto.setIdCancion(documento.getString("idCancion"));
        dto.setNombre(documento.getString("nombre"));
        dto.setImagen(documento.getString("imagen"));
        dto.setIdGenero(documento.getString("idGenero"));
        dto.setNombreArtista(documento.getString("nombreArtista"));
        dto.setNombreAlbum(documento.getString("nombreAlbum"));

        Object fecha = documento.get("fechaAgregacion");

        if (fecha instanceof LocalDate) {
            dto.setFechaAgregacion((LocalDate) fecha);
        }

        return dto;
    }

    private ObjectId convertirObjectId(String id, String campo) throws NegocioException {
        if (id == null || id.isBlank()) {
            throw new NegocioException("El id de " + campo + " es obligatorio.");
        }

        if (!ObjectId.isValid(id.trim())) {
            throw new NegocioException("El id de " + campo + " no es válido.");
        }

        return new ObjectId(id.trim());
    }

    private String normalizarTipoBusqueda(String tipo) throws NegocioException {
        if (tipo == null || tipo.isBlank()) {
            return "TODOS";
        }

        String t = tipo.trim().toUpperCase();

        if (t.equals("TODO") || t.equals("TODOS")) {
            return "TODOS";
        }

        if (t.equals("ARTISTA") || t.equals("ARTISTAS")) {
            return "ARTISTA";
        }

        if (t.equals("ALBUM") || t.equals("ALBUMES") || t.equals("ÁLBUM") || t.equals("ÁLBUMES")) {
            return "ALBUM";
        }

        if (t.equals("CANCION") || t.equals("CANCIONES") || t.equals("CANCIÓN")) {
            return "CANCION";
        }

        throw new NegocioException("Tipo de búsqueda no válido.");
    }

    private String normalizarTipoObligatorio(String tipo) throws NegocioException {
        if (tipo == null || tipo.isBlank()) {
            throw new NegocioException("El tipo de favorito es obligatorio.");
        }

        String t = tipo.trim().toUpperCase();

        if (t.equals("ARTISTA") || t.equals("ARTISTAS")) {
            return "ARTISTA";
        }

        if (t.equals("ALBUM") || t.equals("ALBUMES") || t.equals("ÁLBUM") || t.equals("ÁLBUMES")) {
            return "ALBUM";
        }

        if (t.equals("CANCION") || t.equals("CANCIONES") || t.equals("CANCIÓN")) {
            return "CANCION";
        }

        throw new NegocioException("Tipo de favorito no válido.");
    }

    @Override
    public boolean agregarArtistaFavorito(
            String idUsuario,
            String idArtista
    ) throws NegocioException {

        ObjectId usuario
                = convertirObjectId(
                        idUsuario,
                        "usuario"
                );

        ObjectId artista
                = convertirObjectId(
                        idArtista,
                        "artista"
                );

        try {
            validarGeneroPermitido(
                    usuario,
                    "ARTISTA",
                    artista
            );

            return favoritoDAO.agregarArtista(
                    usuario,
                    artista
            );

        } catch (PersistenciaException ex) {
            throw new NegocioException(
                    ex.getMessage()
            );
        }
    }

    private void validarGeneroPermitido(
            ObjectId idUsuario,
            String tipo,
            ObjectId idElemento
    ) throws PersistenciaException,
            NegocioException {

        ObjectId idGenero
                = favoritoDAO.obtenerGeneroElemento(
                        tipo,
                        idElemento
                );

        if (idGenero == null) {
            throw new NegocioException(
                    "No se pudo identificar el "
                    + "género del elemento."
            );
        }

        boolean noDeseado
                = usuarioDAO.tieneGeneroNoDeseado(
                        idUsuario,
                        idGenero
                );

        if (noDeseado) {
            throw new NegocioException(
                    "No puedes agregar este elemento "
                    + "a favoritos porque pertenece "
                    + "a un género no deseado."
            );
        }
    }
}
