/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import DTO.GeneroNoDeseadoDTO;
import Entidades.Genero;
import Entidades.GeneroNoDeseado;
import Excepciones.NegocioException;
import Excepciones.PersistenciaException;
import Interfaces.IGeneroDAO;
import Interfaces.IUsuarioDAO;
import Persistencia.GeneroDAO;
import Persistencia.UsuarioDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
/**
 *
 * @author Andre
 */
public class GeneroNoDeseadoBO implements IGeneroNoDeseadoBO {

    private final IUsuarioDAO usuarioDAO;
    private final IGeneroDAO generoDAO;

    public GeneroNoDeseadoBO() {
        this.usuarioDAO = new UsuarioDAO();
        this.generoDAO = new GeneroDAO();
    }

    public GeneroNoDeseadoBO(IUsuarioDAO usuarioDAO, IGeneroDAO generoDAO) {
        this.usuarioDAO = usuarioDAO;
        this.generoDAO = generoDAO;
    }

    @Override
    public boolean agregarGenero(String idUsuario, String idGeneroStr) throws NegocioException {
        if (idUsuario == null || idUsuario.isBlank()) {
            throw new NegocioException("El id del usuario es obligatorio.");
        }
        if (idGeneroStr == null || idGeneroStr.isBlank()) {
            throw new NegocioException("Selecciona un género.");
        }

        try {
            ObjectId idUsuarioObj = new ObjectId(idUsuario);
            ObjectId idGeneroObj = new ObjectId(idGeneroStr);

            // Validar que el género exista y tomar su nombre actual
            Genero genero = generoDAO.consultarPorId(idGeneroObj);
            if (genero == null) {
                throw new NegocioException("El género seleccionado no existe.");
            }

            // Validar que no esté duplicado
            if (usuarioDAO.tieneGeneroNoDeseado(idUsuarioObj, idGeneroObj)) {
                throw new NegocioException("Ese género ya está en tu lista de restricciones.");
            }

            GeneroNoDeseado gnd = new GeneroNoDeseado(idGeneroObj, genero.getNombre(), LocalDate.now());
            boolean agregado = usuarioDAO.agregarGeneroNoDeseado(idUsuarioObj, gnd);

            if (!agregado) {
                throw new NegocioException("No se pudo agregar el género a tu lista.");
            }
            return true;

        } catch (PersistenciaException e) {
            throw new NegocioException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new NegocioException("El id proporcionado no es válido.");
        }
    }

    @Override
    public boolean eliminarGenero(String idUsuario, String idGenero) throws NegocioException {
        if (idUsuario == null || idUsuario.isBlank() || idGenero == null || idGenero.isBlank()) {
            throw new NegocioException("Datos incompletos para eliminar el género.");
        }
        try {
            return usuarioDAO.eliminarGeneroNoDeseado(new ObjectId(idUsuario), new ObjectId(idGenero));
        } catch (PersistenciaException e) {
            throw new NegocioException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new NegocioException("El id proporcionado no es válido.");
        }
    }

    @Override
    public List<GeneroNoDeseadoDTO> consultar(String idUsuario) throws NegocioException {
        if (idUsuario == null || idUsuario.isBlank()) {
            throw new NegocioException("El id del usuario es obligatorio.");
        }
        try {
            List<GeneroNoDeseado> lista = usuarioDAO.consultarGenerosNoDeseados(new ObjectId(idUsuario));
            List<GeneroNoDeseadoDTO> dtos = new ArrayList<>();

            for (GeneroNoDeseado gnd : lista) {
                GeneroNoDeseadoDTO dto = new GeneroNoDeseadoDTO();
                dto.setIdGenero(gnd.getIdGenero().toHexString());
                dto.setNombreGenero(gnd.getNombreGenero());
                dto.setFechaAgregacion(gnd.getFechaAgregacion());
                dtos.add(dto);
            }
            return dtos;

        } catch (PersistenciaException e) {
            throw new NegocioException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new NegocioException("El id del usuario no es válido.");
        }
    }
}