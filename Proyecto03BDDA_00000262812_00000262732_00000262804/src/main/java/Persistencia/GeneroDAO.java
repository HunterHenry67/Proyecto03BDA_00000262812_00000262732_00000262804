/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Entidades.Genero;
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
public class GeneroDAO implements IGeneroDAO{
    private final MongoCollection<Genero> coleccion;

    public GeneroDAO() {
        this(new ConexionBD());
    }

    public GeneroDAO(IConexionBD conexionBD) {
        MongoDatabase baseDatos = conexionBD.conexion();
        this.coleccion = baseDatos.getCollection("generos", Genero.class);
    }

    @Override
    public Genero agregar(Genero genero) throws PersistenciaException {
        try {
            if (genero.getId() == null) {
                genero.setId(new ObjectId());
            }

            coleccion.insertOne(genero);
            return genero;

        } catch (Exception e) {
            throw new PersistenciaException("Error al agregar genero: " + e.getMessage());
        }
    }

    @Override
    public Genero consultarPorId(ObjectId idGenero) throws PersistenciaException {
        try {
            return coleccion.find(eq("_id", idGenero)).first();

        } catch (Exception e) {
            throw new PersistenciaException("Error al consultar genero por id: " + e.getMessage());
        }
    }

    @Override
    public Genero consultarPorNombre(String nombre) throws PersistenciaException {
        try {
            return coleccion.find(eq("nombre", nombre)).first();

        } catch (Exception e) {
            throw new PersistenciaException("Error al consultar genero por nombre: " + e.getMessage());
        }
    }

    @Override
    public List<Genero> consultarTodos() throws PersistenciaException {
        try {
            return coleccion.find().into(new ArrayList<>());

        } catch (Exception e) {
            throw new PersistenciaException("Error al consultar generos: " + e.getMessage());
        }
    }

    @Override
    public boolean actualizar(Genero genero) throws PersistenciaException {
        try {
            return coleccion.replaceOne(eq("_id", genero.getId()), genero)
                    .getModifiedCount() > 0;

        } catch (Exception e) {
            throw new PersistenciaException("Error al actualizar genero: " + e.getMessage());
        }
    }

    @Override
    public boolean eliminar(ObjectId idGenero) throws PersistenciaException {
        try {
            return coleccion.deleteOne(eq("_id", idGenero))
                    .getDeletedCount() > 0;

        } catch (Exception e) {
            throw new PersistenciaException("Error al eliminar genero: " + e.getMessage());
        }
    }

    @Override
    public boolean existeNombre(String nombre) throws PersistenciaException {
        return consultarPorNombre(nombre) != null;
    }

}
