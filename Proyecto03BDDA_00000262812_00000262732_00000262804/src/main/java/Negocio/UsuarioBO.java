/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Andre
 */
package Negocio;

import DTO.GeneroNoDeseadoDTO;
import DTO.UsuarioDTO;
import Entidades.GeneroNoDeseado;
import Entidades.Usuario;
import Excepciones.NegocioException;
import Excepciones.PersistenciaException;
import Interfaces.IUsuarioDAO;
import Persistencia.UsuarioDAO;
import Utilerias.Criptografia;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

public class UsuarioBO implements IUsuarioBO {

    private final IUsuarioDAO usuarioDAO;

    public UsuarioBO() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public UsuarioBO(IUsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public UsuarioDTO registrar(UsuarioDTO usuarioDTO) throws NegocioException {
        Usuario usuario = mapearAEntidad(usuarioDTO);
        validarRegistro(usuario);

        try {
            String nombreUsuario = usuario.getNombreUsuario().trim();
            String correo = usuario.getCorreo().trim().toLowerCase();

            if (usuarioDAO.existeNombreUsuario(nombreUsuario)) {
                throw new NegocioException("El nombre de usuario ya existe.");
            }

            if (usuarioDAO.existeCorreo(correo)) {
                throw new NegocioException("El correo ya existe.");
            }

            String sal = Criptografia.generarSal();
            String hash = Criptografia.encriptar(usuario.getContrasena(), sal);

            usuario.setNombreUsuario(nombreUsuario);
            usuario.setCorreo(correo);
            usuario.setContrasena(sal + ":" + hash);

            Usuario usuarioRegistrado = usuarioDAO.agregar(usuario);
            return mapearADTO(usuarioRegistrado);

        } catch (PersistenciaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @Override
    public UsuarioDTO iniciarSesion(String usuarioOCorreo, String contrasena) throws NegocioException {
        validarLogin(usuarioOCorreo, contrasena);

        try {
            Usuario usuario;
            usuarioOCorreo = usuarioOCorreo.trim();

            if (usuarioOCorreo.contains("@")) {
                usuario = usuarioDAO.consultarPorCorreo(usuarioOCorreo.toLowerCase());
            } else {
                usuario = usuarioDAO.consultarPorNombreUsuario(usuarioOCorreo);
            }

            if (usuario == null) {
                throw new NegocioException("Usuario no encontrado.");
            }

            String contrasenaGuardada = usuario.getContrasena();

            if (contrasenaGuardada == null || !contrasenaGuardada.contains(":")) {
                throw new NegocioException("La contraseña almacenada no tiene un formato válido.");
            }

            String[] partes = contrasenaGuardada.split(":", 2);

            String sal = partes[0];
            String hashGuardado = partes[1];

            if (!Criptografia.verificar(contrasena, sal, hashGuardado)) {
                throw new NegocioException("Contraseña incorrecta.");
            }

            return mapearADTO(usuario);

        } catch (PersistenciaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @Override
    public boolean actualizarPerfil(UsuarioDTO usuarioDTO) throws NegocioException {
        Usuario usuario = mapearAEntidad(usuarioDTO);

        if (usuario == null || usuario.getId() == null) {
            throw new NegocioException("No se pudo identificar el usuario.");
        }

        try {
            return usuarioDAO.actualizarPerfil(usuario);

        } catch (PersistenciaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @Override
    public boolean agregarGeneroNoDeseado(String idUsuario, GeneroNoDeseadoDTO generoNoDeseadoDTO) throws NegocioException {
        if (idUsuario == null || idUsuario.isBlank()) {
            throw new NegocioException("El id del usuario es obligatorio.");
        }

        if (generoNoDeseadoDTO == null) {
            throw new NegocioException("El género no deseado es obligatorio.");
        }

        try {
            ObjectId objectIdUsuario = new ObjectId(idUsuario);
            GeneroNoDeseado generoNoDeseado = mapearGeneroNoDeseadoAEntidad(generoNoDeseadoDTO);

            return usuarioDAO.agregarGeneroNoDeseado(objectIdUsuario, generoNoDeseado);

        } catch (PersistenciaException e) {
            throw new NegocioException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new NegocioException("El id del usuario no es válido.");
        }
    }

    @Override
    public boolean eliminarGeneroNoDeseado(String idUsuario, String idGeneroNoDeseado) throws NegocioException {
        if (idUsuario == null || idUsuario.isBlank()) {
            throw new NegocioException("El id del usuario es obligatorio.");
        }

        if (idGeneroNoDeseado == null || idGeneroNoDeseado.isBlank()) {
            throw new NegocioException("El id del género no deseado es obligatorio.");
        }

        try {
            return usuarioDAO.eliminarGeneroNoDeseado(
                    new ObjectId(idUsuario),
                    new ObjectId(idGeneroNoDeseado)
            );

        } catch (PersistenciaException e) {
            throw new NegocioException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new NegocioException("El id no es válido.");
        }
    }

    @Override
    public List<GeneroNoDeseadoDTO> consultarGenerosNoDeseados(String idUsuario) throws NegocioException {
        if (idUsuario == null || idUsuario.isBlank()) {
            throw new NegocioException("El id del usuario es obligatorio.");
        }

        try {
            List<GeneroNoDeseado> generos = usuarioDAO.consultarGenerosNoDeseados(new ObjectId(idUsuario));
            List<GeneroNoDeseadoDTO> generosDTO = new ArrayList<>();

            for (GeneroNoDeseado genero : generos) {
                generosDTO.add(mapearGeneroNoDeseadoADTO(genero));
            }

            return generosDTO;

        } catch (PersistenciaException e) {
            throw new NegocioException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new NegocioException("El id del usuario no es válido.");
        }
    }

    private UsuarioDTO mapearADTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioDTO dto = new UsuarioDTO();

        if (usuario.getId() != null) {
            dto.setId(usuario.getId().toHexString());
        }

        dto.setNombreUsuario(usuario.getNombreUsuario());
        dto.setCorreo(usuario.getCorreo());
        dto.setContrasena(usuario.getContrasena());
        dto.setImagenPerfil(usuario.getImagenPerfil());

        if (usuario.getGeneroNoDeseado() != null) {
            List<GeneroNoDeseadoDTO> generosDTO = new ArrayList<>();

            for (GeneroNoDeseado genero : usuario.getGeneroNoDeseado()) {
                generosDTO.add(mapearGeneroNoDeseadoADTO(genero));
            }

            dto.setGeneroNoDeseado(generosDTO);
        }

        return dto;
    }

    private Usuario mapearAEntidad(UsuarioDTO dto) {
        if (dto == null) {
            return null;
        }

        Usuario usuario = new Usuario();

        if (dto.getId() != null && !dto.getId().isBlank()) {
            usuario.setId(new ObjectId(dto.getId()));
        }

        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setCorreo(dto.getCorreo());
        usuario.setContrasena(dto.getContrasena());
        usuario.setImagenPerfil(dto.getImagenPerfil());

        if (dto.getGeneroNoDeseado() != null) {
            List<GeneroNoDeseado> generos = new ArrayList<>();

            for (GeneroNoDeseadoDTO generoDTO : dto.getGeneroNoDeseado()) {
                generos.add(mapearGeneroNoDeseadoAEntidad(generoDTO));
            }

            usuario.setGeneroNoDeseado(generos);
        }

        return usuario;
    }

    private GeneroNoDeseadoDTO mapearGeneroNoDeseadoADTO(GeneroNoDeseado genero) {
        if (genero == null) {
            return null;
        }

        GeneroNoDeseadoDTO dto = new GeneroNoDeseadoDTO();

        if (genero.getIdGenero() != null) {
            dto.setIdGenero(genero.getIdGenero().toHexString());
        }

        dto.setFechaAgregacion(genero.getFechaAgregacion());

        return dto;
    }

    private GeneroNoDeseado mapearGeneroNoDeseadoAEntidad(GeneroNoDeseadoDTO dto) {
        if (dto == null) {
            return null;
        }

        GeneroNoDeseado genero = new GeneroNoDeseado();

        if (dto.getIdGenero() != null && !dto.getIdGenero().isBlank()) {
            genero.setIdGenero(new ObjectId(dto.getIdGenero()));
        }

        genero.setFechaAgregacion(dto.getFechaAgregacion());

        return genero;
    }

    private void validarRegistro(Usuario usuario) throws NegocioException {
        if (usuario == null) {
            throw new NegocioException("El usuario no puede estar vacío.");
        }

        if (usuario.getNombreUsuario() == null || usuario.getNombreUsuario().isBlank()) {
            throw new NegocioException("Ingrese un nombre de usuario.");
        }

        if (usuario.getCorreo() == null || usuario.getCorreo().isBlank()) {
            throw new NegocioException("Ingrese un correo.");
        }

        if (!usuario.getCorreo().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new NegocioException("Ingrese un correo válido.");
        }

        if (usuario.getContrasena() == null || usuario.getContrasena().isBlank()) {
            throw new NegocioException("Ingrese una contraseña.");
        }
    }

    private void validarLogin(String usuarioOCorreo, String contrasena) throws NegocioException {
        if (usuarioOCorreo == null || usuarioOCorreo.isBlank()) {
            throw new NegocioException("Ingrese usuario o correo.");
        }

        if (contrasena == null || contrasena.isBlank()) {
            throw new NegocioException("Ingrese la contraseña.");
        }
    }
}
