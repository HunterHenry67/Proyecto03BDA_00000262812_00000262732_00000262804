/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Interfaces.IGeneroDAO;
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
        if (genero == null) {
            throw new PersistenciaException(
                    "El género no puede ser nulo."
            );
        }

        if (genero.getNombre() == null
                || genero.getNombre().trim().isEmpty()) {

            throw new PersistenciaException(
                    "El nombre del género no puede estar vacío."
            );
        }

        if (genero.getId() == null) {
            genero.setId(new ObjectId());
        }

        coleccion.insertOne(genero);

        return genero;

    } catch (Exception ex) {
        throw new PersistenciaException(
                "Error al agregar el género: " + ex.getMessage()
        );
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


}
