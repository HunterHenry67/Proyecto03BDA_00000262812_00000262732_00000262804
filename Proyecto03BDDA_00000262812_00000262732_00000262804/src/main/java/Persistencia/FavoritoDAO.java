/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Entidades.Album;
import Entidades.AlbumFavorito;
import Entidades.Artista;
import Entidades.ArtistaFavorito;
import Entidades.Cancion;
import Entidades.CancionFavorita;
import Entidades.Favorito;
import Excepciones.PersistenciaException;
import Interfaces.IConexionBD;
import Interfaces.IFavoritoDAO;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.elemMatch;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 *
 * @author Andre
 */
public class FavoritoDAO implements IFavoritoDAO {

    private final MongoCollection<Favorito> coleccion;
    private final MongoCollection<Artista> coleccionArtistas;

    public FavoritoDAO() {
        this(new ConexionBD());
    }

    public FavoritoDAO(IConexionBD conexionBD) {
        MongoDatabase baseDatos = conexionBD.conexion();
        this.coleccion = baseDatos.getCollection("favoritos", Favorito.class);
        this.coleccionArtistas = baseDatos.getCollection("artistas", Artista.class);
    }

    @Override
    public Favorito consultarPorUsuario(ObjectId idUsuario) throws PersistenciaException {
        try {
            return coleccion.find(eq("idUsuario", idUsuario)).first();
        } catch (Exception e) {
            throw new PersistenciaException("Error al consultar favoritos del usuario: " + e.getMessage());
        }
    }

    @Override
    public boolean agregarArtista(ObjectId idUsuario, ObjectId idArtista) throws PersistenciaException {
        try {
            obtenerOCrearFavorito(idUsuario);

            if (existeEnArreglo(idUsuario, "artistas", "idArtista", idArtista)) {
                return false;
            }

            ArtistaFavorito favorito = new ArtistaFavorito(idArtista, LocalDate.now());
            UpdateResult resultado = coleccion.updateOne(
                    eq("idUsuario", idUsuario),
                    Updates.push("artistas", favorito)
            );

            return resultado.getModifiedCount() > 0;
        } catch (Exception e) {
            throw new PersistenciaException("Error al agregar artista a favoritos: " + e.getMessage());
        }
    }

    @Override
    public boolean agregarAlbum(ObjectId idUsuario, ObjectId idAlbum) throws PersistenciaException {
        try {
            obtenerOCrearFavorito(idUsuario);

            if (existeEnArreglo(idUsuario, "albumes", "idAlbum", idAlbum)) {
                return false;
            }

            AlbumFavorito favorito = new AlbumFavorito(idAlbum, LocalDate.now());
            UpdateResult resultado = coleccion.updateOne(
                    eq("idUsuario", idUsuario),
                    Updates.push("albumes", favorito)
            );

            return resultado.getModifiedCount() > 0;
        } catch (Exception e) {
            throw new PersistenciaException("Error al agregar álbum a favoritos: " + e.getMessage());
        }
    }

    @Override
    public boolean agregarCancion(ObjectId idUsuario, ObjectId idCancion) throws PersistenciaException {
        try {
            obtenerOCrearFavorito(idUsuario);

            if (existeEnArreglo(idUsuario, "canciones", "idCancion", idCancion)) {
                return false;
            }

            CancionFavorita favorita = new CancionFavorita(idCancion, LocalDate.now());
            UpdateResult resultado = coleccion.updateOne(
                    eq("idUsuario", idUsuario),
                    Updates.push("canciones", favorita)
            );

            return resultado.getModifiedCount() > 0;
        } catch (Exception e) {
            throw new PersistenciaException("Error al agregar canción a favoritos: " + e.getMessage());
        }
    }

    @Override
    public boolean eliminarArtista(ObjectId idUsuario, ObjectId idArtista) throws PersistenciaException {
        try {
            UpdateResult resultado = coleccion.updateOne(
                    eq("idUsuario", idUsuario),
                    Updates.pull("artistas", eq("idArtista", idArtista))
            );

            return resultado.getModifiedCount() > 0;
        } catch (Exception e) {
            throw new PersistenciaException("Error al eliminar artista de favoritos: " + e.getMessage());
        }
    }

    @Override
    public boolean eliminarAlbum(ObjectId idUsuario, ObjectId idAlbum) throws PersistenciaException {
        try {
            UpdateResult resultado = coleccion.updateOne(
                    eq("idUsuario", idUsuario),
                    Updates.pull("albumes", eq("idAlbum", idAlbum))
            );

            return resultado.getModifiedCount() > 0;
        } catch (Exception e) {
            throw new PersistenciaException("Error al eliminar álbum de favoritos: " + e.getMessage());
        }
    }

    @Override
    public boolean eliminarCancion(ObjectId idUsuario, ObjectId idCancion) throws PersistenciaException {
        try {
            UpdateResult resultado = coleccion.updateOne(
                    eq("idUsuario", idUsuario),
                    Updates.pull("canciones", eq("idCancion", idCancion))
            );

            return resultado.getModifiedCount() > 0;
        } catch (Exception e) {
            throw new PersistenciaException("Error al eliminar canción de favoritos: " + e.getMessage());
        }
    }

    @Override
    public boolean eliminarElemento(ObjectId idUsuario, String tipo, ObjectId idElemento) throws PersistenciaException {
        String tipoNormalizado = normalizarTipo(tipo);

        switch (tipoNormalizado) {
            case "ARTISTA":
                return eliminarArtista(idUsuario, idElemento);

            case "ALBUM":
                return eliminarAlbum(idUsuario, idElemento);

            case "CANCION":
                return eliminarCancion(idUsuario, idElemento);

            default:
                throw new PersistenciaException("Tipo de favorito no válido.");
        }
    }

    @Override
    public boolean eliminarFavoritosPorGenero(ObjectId idUsuario, ObjectId idGenero) throws PersistenciaException {
        try {
            Set<ObjectId> idsArtistas = new HashSet<>();
            Set<ObjectId> idsAlbumes = new HashSet<>();
            Set<ObjectId> idsCanciones = new HashSet<>();

            List<Artista> artistas = coleccionArtistas.find().into(new ArrayList<>());

            for (Artista artista : artistas) {
                if (artista.getIdGenero() != null && artista.getIdGenero().equals(idGenero)) {
                    idsArtistas.add(artista.getId());
                }

                if (artista.getAlbumes() == null) {
                    continue;
                }

                for (Album album : artista.getAlbumes()) {
                    if (album.getIdGenero() != null && album.getIdGenero().equals(idGenero)) {
                        idsAlbumes.add(album.getId());
                    }

                    if (album.getCanciones() == null) {
                        continue;
                    }

                    for (Cancion cancion : album.getCanciones()) {
                        if (cancion.getIdGenero() != null && cancion.getIdGenero().equals(idGenero)) {
                            idsCanciones.add(cancion.getId());
                        }
                    }
                }
            }

            List<Bson> cambios = new ArrayList<>();

            if (!idsArtistas.isEmpty()) {
                cambios.add(Updates.pull("artistas", in("idArtista", idsArtistas)));
            }

            if (!idsAlbumes.isEmpty()) {
                cambios.add(Updates.pull("albumes", in("idAlbum", idsAlbumes)));
            }

            if (!idsCanciones.isEmpty()) {
                cambios.add(Updates.pull("canciones", in("idCancion", idsCanciones)));
            }

            if (cambios.isEmpty()) {
                return false;
            }

            UpdateResult resultado = coleccion.updateOne(
                    eq("idUsuario", idUsuario),
                    Updates.combine(cambios)
            );

            return resultado.getModifiedCount() > 0;
        } catch (Exception e) {
            throw new PersistenciaException("Error al eliminar favoritos por género: " + e.getMessage());
        }
    }

    @Override
    public List<Document> buscarFavoritos(ObjectId idUsuario, String texto, String tipo, ObjectId idGenero) throws PersistenciaException {
        try {
            Favorito favorito = consultarPorUsuario(idUsuario);
            List<Document> resultados = new ArrayList<>();

            if (favorito == null) {
                return resultados;
            }

            Map<ObjectId, LocalDate> artistasFavoritos = mapearArtistasFavoritos(favorito);
            Map<ObjectId, LocalDate> albumesFavoritos = mapearAlbumesFavoritos(favorito);
            Map<ObjectId, LocalDate> cancionesFavoritas = mapearCancionesFavoritas(favorito);

            String textoNormalizado = normalizarTexto(texto);
            String tipoNormalizado = normalizarTipoBusqueda(tipo);

            List<Artista> artistas = coleccionArtistas.find().into(new ArrayList<>());

            for (Artista artista : artistas) {
                if (artista == null) {
                    continue;
                }

                if (artista.getId() != null
                        && artistasFavoritos.containsKey(artista.getId())
                        && coincideTipo(tipoNormalizado, "ARTISTA")
                        && coincideGenero(idGenero, artista.getIdGenero())
                        && coincideTexto(textoNormalizado, artista.getNombre())) {

                    resultados.add(crearDocumentoArtista(
                            artista,
                            artistasFavoritos.get(artista.getId())
                    ));
                }

                if (artista.getAlbumes() == null) {
                    continue;
                }

                for (Album album : artista.getAlbumes()) {
                    if (album == null) {
                        continue;
                    }

                    if (album.getId() != null
                            && albumesFavoritos.containsKey(album.getId())
                            && coincideTipo(tipoNormalizado, "ALBUM")
                            && coincideGenero(idGenero, album.getIdGenero())
                            && coincideTexto(textoNormalizado, album.getNombre(), artista.getNombre())) {

                        resultados.add(crearDocumentoAlbum(
                                artista,
                                album,
                                albumesFavoritos.get(album.getId())
                        ));
                    }

                    if (album.getCanciones() == null) {
                        continue;
                    }

                    for (Cancion cancion : album.getCanciones()) {
                        if (cancion == null) {
                            continue;
                        }

                        if (cancion.getId() != null
                                && cancionesFavoritas.containsKey(cancion.getId())
                                && coincideTipo(tipoNormalizado, "CANCION")
                                && coincideGenero(idGenero, cancion.getIdGenero())
                                && coincideTexto(textoNormalizado, cancion.getNombre(), album.getNombre(), artista.getNombre())) {

                            resultados.add(crearDocumentoCancion(
                                    artista,
                                    album,
                                    cancion,
                                    cancionesFavoritas.get(cancion.getId())
                            ));
                        }
                    }
                }
            }

            resultados.sort((d1, d2) -> {
                LocalDate f1 = obtenerFecha(d1);
                LocalDate f2 = obtenerFecha(d2);

                if (f1 == null && f2 == null) {
                    return 0;
                }
                if (f1 == null) {
                    return 1;
                }
                if (f2 == null) {
                    return -1;
                }

                return f2.compareTo(f1);
            });

            return resultados;
        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar favoritos: " + e.getMessage());
        }
    }

    private Favorito obtenerOCrearFavorito(ObjectId idUsuario) throws PersistenciaException {
        Favorito favorito = consultarPorUsuario(idUsuario);

        if (favorito != null) {
            return favorito;
        }

        favorito = new Favorito(
                new ObjectId(),
                idUsuario,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        coleccion.insertOne(favorito);
        return favorito;
    }

    private boolean existeEnArreglo(ObjectId idUsuario, String arreglo, String campoId, ObjectId idElemento) {
        Bson filtro = and(
                eq("idUsuario", idUsuario),
                elemMatch(arreglo, eq(campoId, idElemento))
        );

        return coleccion.find(filtro).first() != null;
    }

    private Map<ObjectId, LocalDate> mapearArtistasFavoritos(Favorito favorito) {
        Map<ObjectId, LocalDate> mapa = new HashMap<>();

        if (favorito.getArtistas() == null) {
            return mapa;
        }

        for (ArtistaFavorito artista : favorito.getArtistas()) {
            if (artista.getIdArtista() != null) {
                mapa.put(artista.getIdArtista(), artista.getFechaAgregacion());
            }
        }

        return mapa;
    }

    private Map<ObjectId, LocalDate> mapearAlbumesFavoritos(Favorito favorito) {
        Map<ObjectId, LocalDate> mapa = new HashMap<>();

        if (favorito.getAlbumes() == null) {
            return mapa;
        }

        for (AlbumFavorito album : favorito.getAlbumes()) {
            if (album.getIdAlbum() != null) {
                mapa.put(album.getIdAlbum(), album.getFechaAgregacion());
            }
        }

        return mapa;
    }

    private Map<ObjectId, LocalDate> mapearCancionesFavoritas(Favorito favorito) {
        Map<ObjectId, LocalDate> mapa = new HashMap<>();

        if (favorito.getCanciones() == null) {
            return mapa;
        }

        for (CancionFavorita cancion : favorito.getCanciones()) {
            if (cancion.getIdCancion() != null) {
                mapa.put(cancion.getIdCancion(), cancion.getFechaAgregacion());
            }
        }

        return mapa;
    }

    private Document crearDocumentoArtista(Artista artista, LocalDate fechaAgregacion) {
        return new Document("tipo", "ARTISTA")
                .append("idElemento", aHex(artista.getId()))
                .append("idArtista", aHex(artista.getId()))
                .append("idAlbum", null)
                .append("idCancion", null)
                .append("nombre", artista.getNombre())
                .append("imagen", artista.getImagen())
                .append("idGenero", aHex(artista.getIdGenero()))
                .append("fechaAgregacion", fechaAgregacion)
                .append("nombreArtista", artista.getNombre())
                .append("nombreAlbum", null);
    }

    private Document crearDocumentoAlbum(Artista artista, Album album, LocalDate fechaAgregacion) {
        return new Document("tipo", "ALBUM")
                .append("idElemento", aHex(album.getId()))
                .append("idArtista", aHex(artista.getId()))
                .append("idAlbum", aHex(album.getId()))
                .append("idCancion", null)
                .append("nombre", album.getNombre())
                .append("imagen", album.getImagenPortada())
                .append("idGenero", aHex(album.getIdGenero()))
                .append("fechaAgregacion", fechaAgregacion)
                .append("nombreArtista", artista.getNombre())
                .append("nombreAlbum", album.getNombre());
    }

    private Document crearDocumentoCancion(Artista artista, Album album, Cancion cancion, LocalDate fechaAgregacion) {
        return new Document("tipo", "CANCION")
                .append("idElemento", aHex(cancion.getId()))
                .append("idArtista", aHex(artista.getId()))
                .append("idAlbum", aHex(album.getId()))
                .append("idCancion", aHex(cancion.getId()))
                .append("nombre", cancion.getNombre())
                .append("imagen", album.getImagenPortada())
                .append("idGenero", aHex(cancion.getIdGenero()))
                .append("fechaAgregacion", fechaAgregacion)
                .append("nombreArtista", artista.getNombre())
                .append("nombreAlbum", album.getNombre());
    }

    private boolean coincideTipo(String tipoFiltro, String tipoElemento) {
        return "TODOS".equals(tipoFiltro) || tipoElemento.equals(tipoFiltro);
    }

    private boolean coincideGenero(ObjectId idGeneroFiltro, ObjectId idGeneroElemento) {
        if (idGeneroFiltro == null) {
            return true;
        }

        return idGeneroElemento != null && idGeneroElemento.equals(idGeneroFiltro);
    }

    private boolean coincideTexto(String texto, String... valores) {
        if (texto == null || texto.isBlank()) {
            return true;
        }

        for (String valor : valores) {
            if (valor != null && valor.toLowerCase().contains(texto)) {
                return true;
            }
        }

        return false;
    }

    private String normalizarTexto(String texto) {
        if (texto == null) {
            return "";
        }

        return texto.trim().toLowerCase();
    }

    private String normalizarTipoBusqueda(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            return "TODOS";
        }

        String t = tipo.trim().toUpperCase();

        if (t.equals("TODO") || t.equals("TODOS")) {
            return "TODOS";
        }

        if (t.equals("ARTISTAS")) {
            return "ARTISTA";
        }

        if (t.equals("ALBUMES") || t.equals("ÁLBUMES")) {
            return "ALBUM";
        }

        if (t.equals("CANCIONES")) {
            return "CANCION";
        }

        return t;
    }

    private String normalizarTipo(String tipo) {
        if (tipo == null) {
            return "";
        }

        String t = tipo.trim().toUpperCase();

        if (t.equals("ARTISTAS")) {
            return "ARTISTA";
        }

        if (t.equals("ALBUMES") || t.equals("ÁLBUMES")) {
            return "ALBUM";
        }

        if (t.equals("CANCIONES")) {
            return "CANCION";
        }

        return t;
    }

    private String aHex(ObjectId id) {
        if (id == null) {
            return null;
        }

        return id.toHexString();
    }

    private LocalDate obtenerFecha(Document documento) {
        Object fecha = documento.get("fechaAgregacion");

        if (fecha instanceof LocalDate) {
            return (LocalDate) fecha;
        }

        return null;
    }

    @Override
    public ObjectId obtenerGeneroElemento(
            String tipo,
            ObjectId idElemento
    ) throws PersistenciaException {

        try {
            String tipoNormalizado
                    = normalizarTipo(tipo);

            List<Artista> artistas
                    = coleccionArtistas.find()
                            .into(new ArrayList<>());

            for (Artista artista : artistas) {

                if ("ARTISTA".equals(tipoNormalizado)
                        && artista.getId() != null
                        && artista.getId()
                                .equals(idElemento)) {

                    return artista.getIdGenero();
                }

                if (artista.getAlbumes() == null) {
                    continue;
                }

                for (Album album
                        : artista.getAlbumes()) {

                    if ("ALBUM".equals(tipoNormalizado)
                            && album.getId() != null
                            && album.getId()
                                    .equals(idElemento)) {

                        return album.getIdGenero();
                    }

                    if (album.getCanciones() == null) {
                        continue;
                    }

                    for (Cancion cancion
                            : album.getCanciones()) {

                        if ("CANCION".equals(
                                tipoNormalizado
                        )
                                && cancion.getId() != null
                                && cancion.getId()
                                        .equals(idElemento)) {

                            return cancion.getIdGenero();
                        }
                    }
                }
            }

            return null;

        } catch (Exception ex) {
            throw new PersistenciaException(
                    "Error al obtener el género "
                    + "del elemento: "
                    + ex.getMessage()
            );
        }
    }
}
