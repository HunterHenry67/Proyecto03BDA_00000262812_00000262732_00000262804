
import DTO.AlbumDTO;
import Entidades.Album;
import Excepciones.NegocioException;
import Interfaces.IAlbumDAO;
import Negocio.IAlbumBO;
import Persistencia.AlbumDAO;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

public class AlbumBO implements IAlbumBO {
    private final IAlbumDAO albumDAO;

    public AlbumBO() {
        this.albumDAO = new AlbumDAO();
    }

    @Override
    public AlbumDTO consultarPorId(ObjectId idAlbum) throws NegocioException {
        if (idAlbum == null) {
            throw new NegocioException("El ID del álbum no puede ser nulo");
        }

        try {
            Album album = albumDAO.consultarPorId(idAlbum);
            if (album == null) {
                throw new NegocioException("EL album no existe");
            }
            return mapearAAlbumDTO(album);
        } catch (Exception e) {
            throw new NegocioException("Error al consultar el álbum por ID: " + e.getMessage());
        }
    }

    @Override
    public List<AlbumDTO> consultarPorArtista(String idArtista) throws NegocioException {
        if (idArtista == null || !esObjectIdValido(idArtista)) {
            throw new NegocioException("El ID del artista no es válido");
        }

        try {
            List<Album> albumes = albumDAO.consultarPorArtista(new ObjectId(idArtista));
            List<AlbumDTO> albumesDTO = new ArrayList<>();

            if (albumes != null) {
                for (Album album : albumes) {
                    albumesDTO.add(mapearAAlbumDTO(album));
                }
            }
            return albumesDTO;
        } catch (Exception e) {
            throw new NegocioException("Error al consultar álbumes del artista " + e.getMessage());
        }
    }

    @Override
    public List<AlbumDTO> consultarTodos() throws NegocioException {
        try {
            List<Album> albumes = albumDAO.consultarTodos();
            List<AlbumDTO> albumesDTO = new ArrayList<>();

            if (albumes != null) {
                for (Album album : albumes) {
                    albumesDTO.add(mapearAAlbumDTO(album));
                }
            }
            return albumesDTO;
        } catch (Exception e) {
            throw new NegocioException("Error al consultar todos los álbumes " + e.getMessage());
        }
    }

    private AlbumDTO mapearAAlbumDTO(Album album) {
        AlbumDTO dto = new AlbumDTO();
        
        if (album.getId() != null) {
            dto.setIdAlbum(album.getId().toHexString());
        }
        
        dto.setNombre(album.getNombre());
        dto.setImagenPortada(album.getImagenPortada());
        
        if (album.getIdGenero() != null) {
            dto.setGenero(album.getIdGenero().toHexString());
        }
        
        if (album.getFechaLanzamiento() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            dto.setFechaLanzamiento(album.getFechaLanzamiento().format(formatter));
        }

        return dto;
    }

    private boolean esObjectIdValido(String id) {
        return id != null && id.matches("^[0-9a-fA-F]{24}$");
    }
}