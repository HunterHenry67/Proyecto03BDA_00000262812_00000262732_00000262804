/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Entidades.Album;
import Excepciones.PersistenciaException;
import Interfaces.IConexionBD;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author user
 */
public class AlbumDAO implements IAlbumDAO{
    private final MongoCollection<Album> coleccion;

    public AlbumDAO() {
        this(new ConexionBD());
    }

    public AlbumDAO(IConexionBD conexionBD) {
        MongoDatabase baseDatos = conexionBD.conexion();
        this.coleccion = baseDatos.getCollection("albumes", Album.class);
    }


    @Override
    public Album consultarPorId(ObjectId idAlbum) throws PersistenciaException {
        try {
            return coleccion.find(eq("_id", idAlbum)).first();
        } catch (Exception e) {
            throw new PersistenciaException("Error al consultar álbum por id: " + e.getMessage());
        }
    }

    @Override
    public List<Album> consultarPorArtista(ObjectId idArtista) throws PersistenciaException {
        try {
            return coleccion.find(eq("idArtista", idArtista)).into(new ArrayList<>());
        } catch (Exception e) {
            throw new PersistenciaException("Error al consultar álbumes por artista: " + e.getMessage());
        }
    }

    @Override
    public List<Album> consultarTodos() throws PersistenciaException {
        try {
            return coleccion.find().into(new ArrayList<>());
        } catch (Exception e) {
            throw new PersistenciaException("Error al consultar todos los álbumes: " + e.getMessage());
        }
    }

}
