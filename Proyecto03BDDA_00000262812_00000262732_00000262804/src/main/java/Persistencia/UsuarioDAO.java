/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Entidades.GeneroNoDeseado;
import Entidades.Usuario;
import Excepciones.PersistenciaException;
import Interfaces.IConexionBD;
import Interfaces.IUsuarioDAO;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.elemMatch;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.List;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class UsuarioDAO implements IUsuarioDAO {

    private final MongoCollection<Usuario> coleccion;

    public UsuarioDAO() {
        this(new ConexionBD());
    }

    public UsuarioDAO(IConexionBD conexionBD) {
        MongoDatabase baseDatos = conexionBD.conexion();
        this.coleccion = baseDatos.getCollection("usuarios", Usuario.class);
    }

    @Override
    public Usuario agregar(Usuario usuario) throws PersistenciaException {
        try {
            if (usuario.getId() == null) {
                usuario.setId(new ObjectId());
            }

            if (usuario.getGeneroNoDeseado() == null) {
                usuario.setGeneroNoDeseado(new ArrayList<>());
            }

            coleccion.insertOne(usuario);
            return usuario;

        } catch (Exception e) {
            throw new PersistenciaException("Error al agregar usuario: " + e.getMessage());
        }
    }

    @Override
    public Usuario consultarPorId(ObjectId idUsuario) throws PersistenciaException {
        try {
            return coleccion.find(eq("_id", idUsuario)).first();

        } catch (Exception e) {
            throw new PersistenciaException("Error al consultar usuario por id: " + e.getMessage());
        }
    }

    @Override
    public Usuario consultarPorNombreUsuario(String nombreUsuario) throws PersistenciaException {
        try {
            return coleccion.find(eq("nombreUsuario", nombreUsuario)).first();

        } catch (Exception e) {
            throw new PersistenciaException("Error al consultar usuario por nombre: " + e.getMessage());
        }
    }

    @Override
    public Usuario consultarPorCorreo(String correo) throws PersistenciaException {
        try {
            return coleccion.find(eq("correo", correo)).first();

        } catch (Exception e) {
            throw new PersistenciaException("Error al consultar usuario por correo: " + e.getMessage());
        }
    }

    @Override
    public boolean existeNombreUsuario(String nombreUsuario) throws PersistenciaException {
        return consultarPorNombreUsuario(nombreUsuario) != null;
    }

    @Override
    public boolean existeCorreo(String correo) throws PersistenciaException {
        return consultarPorCorreo(correo) != null;
    }

    @Override
    public boolean actualizarPerfil(Usuario usuario) throws PersistenciaException {
        try {
            Bson filtro = eq("_id", usuario.getId());

            Bson cambios = Updates.combine(
                    Updates.set("nombreUsuario", usuario.getNombreUsuario()),
                    Updates.set("correo", usuario.getCorreo()),
                    Updates.set("contrasena", usuario.getContrasena()),
                    Updates.set("imagenPerfil", usuario.getImagenPerfil())
            );

            UpdateResult resultado = coleccion.updateOne(filtro, cambios);
            return resultado.getModifiedCount() > 0;

        } catch (Exception e) {
            throw new PersistenciaException("Error al actualizar perfil: " + e.getMessage());
        }
    }

    @Override
    public boolean agregarGeneroNoDeseado(ObjectId idUsuario, GeneroNoDeseado generoNoDeseado) throws PersistenciaException {
        try {
            if (generoNoDeseado.getIdGenero() == null) {
                throw new PersistenciaException("El género no deseado debe tener un idGenero válido.");
            }
            Bson filtro = eq("_id", idUsuario);
            Bson cambio = Updates.push("generoNoDeseado", generoNoDeseado);
            UpdateResult resultado = coleccion.updateOne(filtro, cambio);
            return resultado.getModifiedCount() > 0;
        } catch (Exception e) {
            throw new PersistenciaException("Error al agregar género no deseado: " + e.getMessage());
        }
    }

    @Override
    public boolean eliminarGeneroNoDeseado(ObjectId idUsuario, ObjectId idGenero) throws PersistenciaException {
        try {
            Bson filtro = eq("_id", idUsuario);
            Bson cambio = Updates.pull("generoNoDeseado", eq("idGenero", idGenero));
            UpdateResult resultado = coleccion.updateOne(filtro, cambio);
            return resultado.getModifiedCount() > 0;
        } catch (Exception e) {
            throw new PersistenciaException("Error al eliminar género no deseado: " + e.getMessage());
        }
    }

    @Override
    public List<GeneroNoDeseado> consultarGenerosNoDeseados(ObjectId idUsuario) throws PersistenciaException {
        try {
            Usuario usuario = consultarPorId(idUsuario);

            if (usuario == null || usuario.getGeneroNoDeseado() == null) {
                return new ArrayList<>();
            }

            return usuario.getGeneroNoDeseado();

        } catch (Exception e) {
            throw new PersistenciaException("Error al consultar géneros no deseados: " + e.getMessage());
        }
    }

    @Override
    public boolean tieneGeneroNoDeseado(ObjectId idUsuario, ObjectId idGenero) throws PersistenciaException {
        try {
            Bson filtro = and(
                    eq("_id", idUsuario),
                    elemMatch("generoNoDeseado", eq("idGenero", idGenero))
            );
            return coleccion.find(filtro).first() != null;
        } catch (Exception e) {
            throw new PersistenciaException("Error al validar género no deseado: " + e.getMessage());
        }
    }
}
