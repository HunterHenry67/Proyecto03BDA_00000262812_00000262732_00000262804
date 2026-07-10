/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package enriquemadridalvarez.proyecto03bdda_00000262812_00000262732_00000262804;

import Entidades.Album;
import Entidades.Artista;
import Entidades.Cancion;
import Entidades.Genero;
import Entidades.Integrante;
import Excepciones.PersistenciaException;
import Interfaces.IArtistaDAO;
import Interfaces.IConexionBD;
import Persistencia.GeneroDAO;
import Interfaces.IGeneroDAO;
import Persistencia.ArtistaDAO;
import Persistencia.ConexionBD;
import Presentacion.frmLogin;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author BALAMRUSH
 */
public class Proyecto03BDDA_00000262812_00000262732_00000262804 {

    public static void main(String[] args) {
        pruebaGeneroDAO();
      new frmLogin();
    }
    
    public static void pruebaGeneroDAO(){
        try{
            IConexionBD conexion = new ConexionBD();
            IArtistaDAO artistaDAO = new ArtistaDAO(conexion);

            List<Integrante> integrantes = new ArrayList<>();

            Integrante integrante = new Integrante();
            integrante.setIdPersona(new ObjectId());
            integrante.setRol("Solista");
            integrante.setFechaIngreso(LocalDate.of(2010, 1, 1));
            integrante.setFechaSalida(null);
            integrante.setActivo(true);

            integrantes.add(integrante);

            List<Cancion> canciones = new ArrayList<>();

            Cancion cancion = new Cancion();
            cancion.setId(new ObjectId());
            cancion.setNombre("Blinding Lights");
            cancion.setDuracion("03:20");
            cancion.setIdGenero(new ObjectId());

            canciones.add(cancion);

            List<Album> albumes = new ArrayList<>();

            Album album = new Album();
            album.setId(new ObjectId());
            album.setNombre("After Hours");
            album.setFechaLanzamiento(null);
            album.setImagenPortada("afterhours.jpg");
            album.setIdGenero(new ObjectId());
            album.setCanciones(canciones);

            albumes.add(album);

            Artista artista = new Artista();
            artista.setId(new ObjectId());
            artista.setNombre("The Weeknd");
            artista.setImagen("theweeknd.jpg");
            artista.setIdGenero(new ObjectId());
            artista.setIntegrantes(integrantes);
            artista.setAlbumes(albumes);


            List<Artista> artistas = new ArrayList<>();
            artistas.add(artista);

            artistaDAO.agregarMasivo(artistas);

            System.out.println("Artista agregado correctamente.");

        } catch (PersistenciaException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
