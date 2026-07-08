/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Entidades.Persona;
import Excepciones.PersistenciaException;
import Interfaces.IConexionBD;
import Interfaces.IPersonaDAO;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author BALAMRUSH
 */
public class PersonaDAO implements IPersonaDAO{
    
    private final MongoCollection<Persona> coleccion;
    
    public PersonaDAO(IConexionBD conexionBD){
        MongoDatabase baseDatos = new ConexionBD().conexion();
        this.coleccion = baseDatos.getCollection("personas", Persona.class);
        
    }

    @Override
    public List<Persona> agregarMasivo(List<Persona> personas) throws PersistenciaException {
        try{                        
            if(personas == null){
                throw new PersistenciaException("La lista está vacía.");
            }
            for(Persona persona: personas){
                if (persona.getId() == null) {
                    persona.setId(new ObjectId());
                }
            }
            coleccion.insertMany(personas);
            return personas;
        }catch(Exception ex){
            throw new PersistenciaException("Error al agregar masivamente las personas: "+ex.getMessage());
        }
    }

    @Override
    public Persona consultarPorId(ObjectId idPersona) throws PersistenciaException {
        try{
            if(idPersona == null){
                throw new PersistenciaException("La persona con el id no existe o no puede ser nulo.");
            }
            return coleccion.find(eq("_id", idPersona)).first();
        }catch(Exception ex){
            throw new PersistenciaException("Error al consultar la persona por id: "+ex.getMessage());
        }
    }  
}
