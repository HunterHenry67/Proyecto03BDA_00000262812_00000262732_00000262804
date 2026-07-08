/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Entidades.Artista;
import Excepciones.PersistenciaException;
import Interfaces.IArtistaDAO;
import Interfaces.IConexionBD;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author BALAMRUSH
 */
public class ArtistaDAO implements IArtistaDAO{

    private MongoCollection<Artista> coleccion;
    
    public ArtistaDAO(IConexionBD conexion){
        MongoDatabase baseDatos = new ConexionBD().conexion();
        this.coleccion = baseDatos.getCollection("artistas", Artista.class);
    }
    @Override
    public List<Artista> agregarMasivo(List<Artista> artistas) throws PersistenciaException {
        try{
            if(artistas == null || artistas.isEmpty()){
                throw new PersistenciaException("La lista de los artistas está vacía");
            }
            coleccion.insertMany(artistas);
            return artistas;
        }catch(Exception ex){
            throw new PersistenciaException("Error al agregar masivamente los artistas: "+ex.getMessage());
        }
    }

    @Override
    public List<Artista> consultarTodos() throws PersistenciaException {
        try{
            return coleccion.find().into(new ArrayList<>());
        }catch(Exception ex){
            throw new PersistenciaException("Error al consultar todos los artistas: "+ex.getMessage());
        }
    }

    @Override
    public Artista consultarPorId(ObjectId idArtista) throws PersistenciaException {
        try {
            if (idArtista == null) {
                throw new PersistenciaException("El id del artista no puede ser nulo.");
            }
            return coleccion.find(eq("_id", idArtista)).first();
        } catch (Exception ex) {
            throw new PersistenciaException("Error al consultar artista: " + ex.getMessage());
        }
    }

    @Override
    public List<Artista> buscarPorNombre(String nombre) throws PersistenciaException {
        try{
            return coleccion.find(regex("nombre", nombre, "i" )).into(new ArrayList<>());
        }catch(Exception ex){
            throw new PersistenciaException("Error al buscarel artista por nombre: "+ex.getMessage());
        }
    }

    @Override
    public List<Artista> buscarPorGenero(ObjectId idGenero) throws PersistenciaException {
        try{
            return coleccion.find(eq("idGenero", idGenero)).into(new ArrayList<>());
        }catch(Exception ex){
            throw new PersistenciaException("Error al buscar el artista por género: "+ex.getMessage());
        }
    }
    
}
