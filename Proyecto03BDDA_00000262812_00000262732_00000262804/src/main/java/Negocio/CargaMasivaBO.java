/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import DTO.AlbumDTO;
import DTO.ArtistaDTO;
import DTO.CancionDTO;
import DTO.GeneroDTO;
import DTO.IntegranteDTO;
import DTO.PersonaDTO;
import Excepciones.NegocioException;
import Excepciones.PersistenciaException;
import Interfaces.IArtistaBO;
import Interfaces.IGeneroBO;
import Interfaces.IPersonaBO;
import Persistencia.ConexionBD;
import Persistencia.PersonaDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;

/**
 * Carga masiva de un catálogo 
 * @author BALAMRUSH
 */
public class CargaMasivaBO {

    private final IGeneroBO generoBO;
    private final IPersonaBO personaBO;
    private final IArtistaBO artistaBO;

    private Map<String, String> idGeneros;

    public CargaMasivaBO() {
        this.generoBO = new GeneroBO();
        this.personaBO = new PersonaBO(new PersonaDAO(new ConexionBD()));
        this.artistaBO = new ArtistaBO();
    }

    /**
     * Ejecuta la carga masiva completa.
     *
     * @return mensaje con el resumen de lo insertado.
     */
    public String ejecutarCargaMasiva() throws NegocioException {
        this.idGeneros = resolverGeneros();

        List<PersonaDTO> personas = new ArrayList<>();
        List<ArtistaDTO> artistas = new ArrayList<>();

        artistas.add(crearLinkinPark(personas));
        artistas.add(crearColdplay(personas));
        artistas.add(crearDaftPunk(personas));
        artistas.add(crearImagineDragons(personas));
        artistas.add(crearArcticMonkeys(personas));
        artistas.add(crearMetallica(personas));
        artistas.add(crearQueen(personas));

        artistas.add(crearDuaLipa(personas));
        artistas.add(crearBillieEilish(personas));
        artistas.add(crearBrunoMars(personas));
        artistas.add(crearBadBunny(personas));
        artistas.add(crearShakira(personas));
        artistas.add(crearTaylorSwift(personas));
        artistas.add(crearAdele(personas));
        artistas.add(crearTheWeeknd(personas));

        try {
            List<PersonaDTO> personasGuardadas = personaBO.agregarMasivo(personas);
            List<ArtistaDTO> artistasGuardados = artistaBO.agregarMasivo(artistas);

            return "Se agregaron " + personasGuardadas.size() + " personas/integrantes y "
                    + artistasGuardados.size() + " artistas (con sus álbumes y canciones).";

        } catch (NegocioException ex) {
            throw new NegocioException("Error al realizar la carga masiva: " + ex.getMessage());
        }
    }


    private Map<String, String> resolverGeneros() throws NegocioException {
        List<GeneroDTO> todos;
        try {
            todos = generoBO.consultarTodos();
        } catch (PersistenciaException ex) {
            throw new NegocioException("No se pudieron consultar los géneros: " + ex.getMessage());
        }

        Map<String, String> porNombre = new HashMap<>();
        if (todos != null) {
            for (GeneroDTO genero : todos) {
                porNombre.put(normalizar(genero.getNombre()), genero.getId());
            }
        }

        Map<String, String> resultado = new HashMap<>();
        resultado.put("ROCK", buscarGenero(porNombre, "Rock"));
        resultado.put("POP", buscarGenero(porNombre, "Pop"));
        resultado.put("ELECTRONICA", buscarGenero(porNombre, "Electrónica", "Electronica", "Electronic"));
        resultado.put("RNB", buscarGenero(porNombre, "R&B", "RnB", "R & B"));
        resultado.put("LATINO", buscarGenero(porNombre, "Reggaetón", "Reggaeton", "Latino", "Urbano"));
        return resultado;
    }

    private String buscarGenero(Map<String, String> porNombre, String... candidatos) throws NegocioException {
        for (String candidato : candidatos) {
            String id = porNombre.get(normalizar(candidato));
            if (id != null) {
                return id;
            }
        }
        throw new NegocioException(
                "No se encontró el género '" + candidatos[0] + "' en la base de datos. "
                + "Agrega ese género antes de hacer la carga masiva."
        );
    }

    private String normalizar(String texto) {
        return texto == null ? "" : texto.trim().toLowerCase();
    }


    private String nuevaPersona(List<PersonaDTO> personas, String nombre, String descripcion, String imagen) {
        PersonaDTO persona = new PersonaDTO();
        String id = new ObjectId().toHexString();
        persona.setId(id);
        persona.setNombre(nombre);
        persona.setDescripcion(descripcion);
        persona.setImagen(imagen);
        personas.add(persona);
        return id;
    }

    private IntegranteDTO integrante(String idPersona, String rol, LocalDate ingreso, LocalDate salida, boolean activo) {
        return new IntegranteDTO(idPersona, rol, ingreso, salida, activo);
    }

    private CancionDTO cancion(String nombre, String duracion, String idGenero) {
        return new CancionDTO(nombre, duracion, idGenero);
    }

    private AlbumDTO album(String nombre, String fechaLanzamiento, String idGenero, String imagenPortada, CancionDTO... canciones) {
        AlbumDTO album = new AlbumDTO();
        album.setNombre(nombre);
        album.setFechaLanzamiento(fechaLanzamiento);
        album.setIdGenero(idGenero);
        album.setImagenPortada(imagenPortada);
        List<CancionDTO> lista = new ArrayList<>();
        for (CancionDTO c : canciones) {
            lista.add(c);
        }
        album.setCanciones(lista);
        return album;
    }


    private ArtistaDTO crearLinkinPark(List<PersonaDTO> personas) {
        String genero = idGeneros.get("ROCK");

        String chester = nuevaPersona(personas, "Chester Bennington", "Vocalista original de Linkin Park.", "imagenes/integrantes/chester_bennington.jpg");
        String mike = nuevaPersona(personas, "Mike Shinoda", "Vocalista, rapero y guitarrista de Linkin Park.", "imagenes/integrantes/mike_shinoda.jpg");
        String brad = nuevaPersona(personas, "Brad Delson", "Guitarrista de Linkin Park.", "imagenes/integrantes/brad_delson.jpg");
        String dave = nuevaPersona(personas, "Dave Farrell", "Bajista de Linkin Park.", "imagenes/integrantes/dave_farrell.jpg");
        String joe = nuevaPersona(personas, "Joe Hahn", "DJ y turntablist de Linkin Park.", "imagenes/integrantes/joe_hahn.jpg");
        String emily = nuevaPersona(personas, "Emily Armstrong", "Vocalista de Linkin Park desde 2024.", "imagenes/integrantes/emily_armstrong.jpg");

        List<IntegranteDTO> integrantes = List.of(
                integrante(chester, "Vocalista", LocalDate.of(1999, 1, 1), LocalDate.of(2017, 7, 20), false),
                integrante(mike, "Vocalista / Guitarrista", LocalDate.of(1996, 1, 1), null, true),
                integrante(brad, "Guitarrista", LocalDate.of(1996, 1, 1), null, true),
                integrante(dave, "Bajista", LocalDate.of(1996, 1, 1), null, true),
                integrante(joe, "DJ / Turntables", LocalDate.of(1996, 1, 1), null, true),
                integrante(emily, "Vocalista", LocalDate.of(2024, 9, 5), null, true)
        );

        List<AlbumDTO> albumes = List.of(
                album("Hybrid Theory", "2000-10-24", genero, "imagenes/albumes/hybrid_theory.jpg",
                        cancion("In the End", "03:36", genero),
                        cancion("One Step Closer", "02:35", genero)),
                album("Meteora", "2003-03-25", genero, "imagenes/albumes/meteora.jpg",
                        cancion("Numb", "03:07", genero),
                        cancion("Faint", "02:42", genero))
        );

        return new ArtistaDTO("Linkin Park", "imagenes/bandas/linkin_park.jpg", genero, integrantes, albumes);
    }

    private ArtistaDTO crearColdplay(List<PersonaDTO> personas) {
        String genero = idGeneros.get("ROCK");

        String chris = nuevaPersona(personas, "Chris Martin", "Vocalista y pianista de Coldplay.", "imagenes/integrantes/chris_martin.jpg");
        String jonny = nuevaPersona(personas, "Jonny Buckland", "Guitarrista de Coldplay.", "imagenes/integrantes/jonny_buckland.jpg");
        String guy = nuevaPersona(personas, "Guy Berryman", "Bajista de Coldplay.", "imagenes/integrantes/guy_berryman.jpg");
        String will = nuevaPersona(personas, "Will Champion", "Batería de Coldplay.", "imagenes/integrantes/will_champion.jpg");

        List<IntegranteDTO> integrantes = List.of(
                integrante(chris, "Vocalista / Piano", LocalDate.of(1996, 1, 1), null, true),
                integrante(jonny, "Guitarrista", LocalDate.of(1996, 1, 1), null, true),
                integrante(guy, "Bajista", LocalDate.of(1996, 1, 1), null, true),
                integrante(will, "Batería", LocalDate.of(1997, 1, 1), null, true)
        );

        List<AlbumDTO> albumes = List.of(
                album("Parachutes", "2000-07-10", genero, "imagenes/albumes/parachutes.jpg",
                        cancion("Yellow", "04:29", genero),
                        cancion("Trouble", "04:33", genero)),
                album("Music of the Spheres", "2021-10-15", genero, "imagenes/albumes/music_of_the_spheres.jpg",
                        cancion("Higher Power", "03:29", genero),
                        cancion("My Universe", "03:52", genero))
        );

        return new ArtistaDTO("Coldplay", "imagenes/bandas/coldplay.jpg", genero, integrantes, albumes);
    }

    private ArtistaDTO crearDaftPunk(List<PersonaDTO> personas) {
        String genero = idGeneros.get("ELECTRONICA");

        String thomas = nuevaPersona(personas, "Thomas Bangalter", "Productor y DJ, mitad de Daft Punk.", "imagenes/integrantes/thomas_bangalter.jpg");
        String guyManuel = nuevaPersona(personas, "Guy-Manuel de Homem-Christo", "Productor y DJ, mitad de Daft Punk.", "imagenes/integrantes/guy_manuel.jpg");

        List<IntegranteDTO> integrantes = List.of(
                integrante(thomas, "Productor / DJ", LocalDate.of(1993, 1, 1), null, true),
                integrante(guyManuel, "Productor / DJ", LocalDate.of(1993, 1, 1), null, true)
        );

        List<AlbumDTO> albumes = List.of(
                album("Discovery", "2001-03-12", genero, "imagenes/albumes/discovery.jpg",
                        cancion("One More Time", "05:20", genero),
                        cancion("Digital Love", "04:58", genero)),
                album("Random Access Memories", "2013-05-17", genero, "imagenes/albumes/random_access_memories.jpg",
                        cancion("Get Lucky", "04:08", genero),
                        cancion("Instant Crush", "03:37", genero))
        );

        return new ArtistaDTO("Daft Punk", "imagenes/bandas/daft_punk.jpg", genero, integrantes, albumes);
    }

    private ArtistaDTO crearImagineDragons(List<PersonaDTO> personas) {
        String genero = idGeneros.get("ROCK");

        String dan = nuevaPersona(personas, "Dan Reynolds", "Vocalista de Imagine Dragons.", "imagenes/integrantes/dan_reynolds.jpg");
        String wayne = nuevaPersona(personas, "Wayne Sermon", "Guitarrista de Imagine Dragons.", "imagenes/integrantes/wayne_sermon.jpg");
        String ben = nuevaPersona(personas, "Ben McKee", "Bajista de Imagine Dragons.", "imagenes/integrantes/ben_mckee.jpg");
        String daniel = nuevaPersona(personas, "Daniel Platzman", "Batería de Imagine Dragons.", "imagenes/integrantes/daniel_platzman.jpg");

        List<IntegranteDTO> integrantes = List.of(
                integrante(dan, "Vocalista", LocalDate.of(2008, 1, 1), null, true),
                integrante(wayne, "Guitarrista", LocalDate.of(2008, 1, 1), null, true),
                integrante(ben, "Bajista", LocalDate.of(2010, 1, 1), null, true),
                integrante(daniel, "Batería", LocalDate.of(2011, 1, 1), null, true)
        );

        List<AlbumDTO> albumes = List.of(
                album("Night Visions", "2012-09-04", genero, "imagenes/albumes/night_visions.jpg",
                        cancion("Radioactive", "03:07", genero),
                        cancion("Demons", "02:57", genero)),
                album("Evolve", "2017-06-23", genero, "imagenes/albumes/evolve.jpg",
                        cancion("Believer", "03:24", genero),
                        cancion("Thunder", "03:07", genero))
        );

        return new ArtistaDTO("Imagine Dragons", "imagenes/bandas/imagine_dragons.jpg", genero, integrantes, albumes);
    }

    private ArtistaDTO crearArcticMonkeys(List<PersonaDTO> personas) {
        String genero = idGeneros.get("ROCK");

        String alex = nuevaPersona(personas, "Alex Turner", "Vocalista y guitarrista de Arctic Monkeys.", "imagenes/integrantes/alex_turner.jpg");
        String jamie = nuevaPersona(personas, "Jamie Cook", "Guitarrista de Arctic Monkeys.", "imagenes/integrantes/jamie_cook.jpg");
        String matt = nuevaPersona(personas, "Matt Helders", "Batería de Arctic Monkeys.", "imagenes/integrantes/matt_helders.jpg");
        String nick = nuevaPersona(personas, "Nick O'Malley", "Bajista de Arctic Monkeys.", "imagenes/integrantes/nick_omalley.jpg");

        List<IntegranteDTO> integrantes = List.of(
                integrante(alex, "Vocalista / Guitarrista", LocalDate.of(2002, 1, 1), null, true),
                integrante(jamie, "Guitarrista", LocalDate.of(2002, 1, 1), null, true),
                integrante(matt, "Batería", LocalDate.of(2002, 1, 1), null, true),
                integrante(nick, "Bajista", LocalDate.of(2006, 1, 1), null, true)
        );

        List<AlbumDTO> albumes = List.of(
                album("AM", "2013-09-09", genero, "imagenes/albumes/am.jpg",
                        cancion("Do I Wanna Know?", "04:32", genero),
                        cancion("R U Mine?", "03:21", genero)),
                album("The Car", "2022-10-21", genero, "imagenes/albumes/the_car.jpg",
                        cancion("There'd Better Be a Mirrorball", "04:32", genero),
                        cancion("Body Paint", "04:20", genero))
        );

        return new ArtistaDTO("Arctic Monkeys", "imagenes/bandas/artic_monkeys.jpg", genero, integrantes, albumes);
    }

    private ArtistaDTO crearMetallica(List<PersonaDTO> personas) {
        String genero = idGeneros.get("ROCK");

        String james = nuevaPersona(personas, "James Hetfield", "Vocalista y guitarrista de Metallica.", "imagenes/integrantes/james_hetfield.jpg");
        String lars = nuevaPersona(personas, "Lars Ulrich", "Batería de Metallica.", "imagenes/integrantes/lars_ulrich.jpg");
        String kirk = nuevaPersona(personas, "Kirk Hammett", "Guitarrista de Metallica.", "imagenes/integrantes/kirk_hammett.jpg");
        String robert = nuevaPersona(personas, "Robert Trujillo", "Bajista de Metallica.", "imagenes/integrantes/robert_trujillo.jpg");

        List<IntegranteDTO> integrantes = List.of(
                integrante(james, "Vocalista / Guitarrista", LocalDate.of(1981, 10, 28), null, true),
                integrante(lars, "Batería", LocalDate.of(1981, 10, 28), null, true),
                integrante(kirk, "Guitarrista", LocalDate.of(1983, 4, 1), null, true),
                integrante(robert, "Bajista", LocalDate.of(2003, 2, 24), null, true)
        );

        List<AlbumDTO> albumes = List.of(
                album("Master of Puppets", "1986-03-03", genero, "imagenes/albumes/master_of_puppets.jpg",
                        cancion("Master of Puppets", "08:35", genero),
                        cancion("Battery", "05:12", genero)),
                album("72 Seasons", "2023-04-14", genero, "imagenes/albumes/72_seasons.jpg",
                        cancion("Lux Æterna", "03:19", genero),
                        cancion("72 Seasons", "05:11", genero))
        );

        return new ArtistaDTO("Metallica", "imagenes/bandas/metallica.jpg", genero, integrantes, albumes);
    }

    private ArtistaDTO crearQueen(List<PersonaDTO> personas) {
        String genero = idGeneros.get("ROCK");

        String freddie = nuevaPersona(personas, "Freddie Mercury", "Vocalista original de Queen.", "imagenes/integrantes/freddie_mercury.jpg");
        String brian = nuevaPersona(personas, "Brian May", "Guitarrista de Queen.", "imagenes/integrantes/brian_may.jpg");
        String roger = nuevaPersona(personas, "Roger Taylor", "Batería de Queen.", "imagenes/integrantes/roger_taylor.jpg");
        String john = nuevaPersona(personas, "John Deacon", "Bajista de Queen.", "imagenes/integrantes/john_deacon.jpg");

        List<IntegranteDTO> integrantes = List.of(
                integrante(freddie, "Vocalista", LocalDate.of(1970, 6, 27), LocalDate.of(1991, 11, 24), false),
                integrante(brian, "Guitarrista", LocalDate.of(1970, 6, 27), null, true),
                integrante(roger, "Batería", LocalDate.of(1970, 6, 27), null, true),
                integrante(john, "Bajista", LocalDate.of(1971, 2, 1), LocalDate.of(1997, 1, 1), false)
        );

        List<AlbumDTO> albumes = List.of(
                album("A Night at the Opera", "1975-11-21", genero, "imagenes/albumes/a_night_at_the_opera.jpg",
                        cancion("Bohemian Rhapsody", "05:55", genero),
                        cancion("You're My Best Friend", "02:52", genero)),
                album("The Game", "1980-06-30", genero, "imagenes/albumes/the_game.jpg",
                        cancion("Another One Bites the Dust", "03:35", genero),
                        cancion("Crazy Little Thing Called Love", "02:43", genero))
        );

        return new ArtistaDTO("Queen", "imagenes/bandas/queen.jpg", genero, integrantes, albumes);
    }


    private ArtistaDTO crearDuaLipa(List<PersonaDTO> personas) {
        String genero = idGeneros.get("POP");
        String persona = nuevaPersona(personas, "Dua Lipa", "Cantante y compositora británica.", "imagenes/personas/dua_lipa.jpg");

        List<IntegranteDTO> integrantes = List.of(
                integrante(persona, "Solista", LocalDate.of(2015, 1, 1), null, true)
        );

        List<AlbumDTO> albumes = List.of(
                album("Future Nostalgia", "2020-03-27", genero, "imagenes/albumes/future_nostalgia.jpg",
                        cancion("Don't Start Now", "03:03", genero),
                        cancion("Levitating", "03:23", genero)),
                album("Radical Optimism", "2024-05-03", genero, "imagenes/albumes/radical_optimism.jpg",
                        cancion("Houdini", "03:07", genero),
                        cancion("Training Season", "03:20", genero))
        );

        return new ArtistaDTO("Dua Lipa", "imagenes/solistas/dua_lipa.jpg", genero, integrantes, albumes);
    }

    private ArtistaDTO crearBillieEilish(List<PersonaDTO> personas) {
        String genero = idGeneros.get("POP");
        String billie = nuevaPersona(personas, "Billie Eilish", "Cantante y compositora estadounidense.", "imagenes/personas/billie_eilish.jpg");
        String finneas = nuevaPersona(personas, "Finneas O'Connell", "Productor y compositor, hermano y colaborador de Billie Eilish.", "imagenes/integrantes/finneas.jpg");

        List<IntegranteDTO> integrantes = List.of(
                integrante(billie, "Vocalista", LocalDate.of(2015, 1, 1), null, true),
                integrante(finneas, "Productor / Compositor", LocalDate.of(2015, 1, 1), null, true)
        );

        List<AlbumDTO> albumes = List.of(
                album("When We All Fall Asleep, Where Do We Go?", "2019-03-29", genero, "imagenes/albumes/when_we_all_fall_asleep_where_do_we_go.jpg",
                        cancion("Bad Guy", "03:14", genero),
                        cancion("Bury a Friend", "03:13", genero)),
                album("Hit Me Hard and Soft", "2024-05-17", genero, "imagenes/albumes/hit_me_hard_and_soft.jpg",
                        cancion("Birds of a Feather", "03:30", genero),
                        cancion("Lunch", "03:20", genero))
        );

        return new ArtistaDTO("Billie Eilish", "imagenes/solistas/billie_eilish.jpg", genero, integrantes, albumes);
    }

    private ArtistaDTO crearBrunoMars(List<PersonaDTO> personas) {
        String genero = idGeneros.get("POP");
        String persona = nuevaPersona(personas, "Bruno Mars", "Cantante, compositor y productor estadounidense.", "imagenes/personas/bruno_mars.jpg");

        List<IntegranteDTO> integrantes = List.of(
                integrante(persona, "Solista", LocalDate.of(2010, 1, 1), null, true)
        );

        List<AlbumDTO> albumes = List.of(
                album("Doo-Wops & Hooligans", "2010-10-04", genero, "imagenes/albumes/doo_wops_and_hooligans.jpg",
                        cancion("Just the Way You Are", "03:40", genero),
                        cancion("Grenade", "03:42", genero)),
                album("24K Magic", "2016-11-18", genero, "imagenes/albumes/24k_magic.jpg",
                        cancion("24K Magic", "03:46", genero),
                        cancion("That's What I Like", "03:26", genero))
        );

        return new ArtistaDTO("Bruno Mars", "imagenes/solistas/bruno_mars.jpg", genero, integrantes, albumes);
    }

    private ArtistaDTO crearBadBunny(List<PersonaDTO> personas) {
        String genero = idGeneros.get("LATINO");
        String persona = nuevaPersona(personas, "Bad Bunny", "Cantante y compositor puertorriqueño de reggaetón.", "imagenes/personas/bad_bunny.jpg");

        List<IntegranteDTO> integrantes = List.of(
                integrante(persona, "Solista", LocalDate.of(2016, 1, 1), null, true)
        );

        List<AlbumDTO> albumes = List.of(
                album("X100PRE", "2018-12-24", genero, "imagenes/albumes/x100pre.jpg",
                        cancion("Estamos Bien", "03:23", genero),
                        cancion("Callaita", "03:59", genero)),
                album("Nadie Sabe Lo Que Va a Pasar Mañana", "2023-10-13", genero, "imagenes/albumes/nadie_sabe_lo_que_va_a_pasar_manana.jpg",
                        cancion("Monaco", "03:12", genero),
                        cancion("Vou 787", "03:31", genero)),
                album("Las Mujeres Ya No Lloran", "2024-10-04", genero, "imagenes/albumes/las_mujeres_ya_no_lloran.jpg",
                        cancion("Voy a Llevarte pa PR", "02:51", genero),
                        cancion("Baticano", "04:03", genero))
        );

        return new ArtistaDTO("Bad Bunny", "imagenes/solistas/bad_bunny.jpg", genero, integrantes, albumes);
    }

    private ArtistaDTO crearShakira(List<PersonaDTO> personas) {
        String genero = idGeneros.get("LATINO");
        String persona = nuevaPersona(personas, "Shakira", "Cantante y compositora colombiana.", "imagenes/personas/shakira.jpg");

        List<IntegranteDTO> integrantes = List.of(
                integrante(persona, "Solista", LocalDate.of(1995, 1, 1), null, true)
        );

        List<AlbumDTO> albumes = List.of(
                album("El Dorado", "2017-05-26", genero, "imagenes/albumes/el_dorado.jpg",
                        cancion("Chantaje", "03:21", genero),
                        cancion("Perro Fiel", "03:24", genero))
        );

        return new ArtistaDTO("Shakira", "imagenes/solistas/shakira.jpg", genero, integrantes, albumes);
    }

    private ArtistaDTO crearTaylorSwift(List<PersonaDTO> personas) {
        String genero = idGeneros.get("POP");
        String persona = nuevaPersona(personas, "Taylor Swift", "Cantante y compositora estadounidense.", "imagenes/personas/taylor_swift.jpg");

        List<IntegranteDTO> integrantes = List.of(
                integrante(persona, "Solista", LocalDate.of(2006, 1, 1), null, true)
        );

        List<AlbumDTO> albumes = List.of(
                album("Midnights", "2022-10-21", genero, "imagenes/albumes/midnights.jpg",
                        cancion("Anti-Hero", "03:20", genero),
                        cancion("Lavender Haze", "03:22", genero)),
                album("1989 (Taylor's Version)", "2023-10-27", genero, "imagenes/albumes/1989_taylors_version.jpg",
                        cancion("Style", "03:51", genero),
                        cancion("Blank Space", "03:51", genero))
        );

        return new ArtistaDTO("Taylor Swift", "imagenes/solistas/taylor_swift.jpg", genero, integrantes, albumes);
    }

    private ArtistaDTO crearAdele(List<PersonaDTO> personas) {
        String genero = idGeneros.get("POP");
        String persona = nuevaPersona(personas, "Adele", "Cantante y compositora británica.", "imagenes/personas/adele.jpg");

        List<IntegranteDTO> integrantes = List.of(
                integrante(persona, "Solista", LocalDate.of(2008, 1, 1), null, true)
        );

        List<AlbumDTO> albumes = List.of(
                album("21", "2011-01-24", genero, "imagenes/albumes/21.jpg",
                        cancion("Rolling in the Deep", "03:48", genero),
                        cancion("Someone Like You", "04:45", genero)),
                album("30", "2021-11-19", genero, "imagenes/albumes/30.jpg",
                        cancion("Easy on Me", "03:44", genero),
                        cancion("Oh My God", "03:39", genero))
        );

        return new ArtistaDTO("Adele", "imagenes/solistas/adele.jpg", genero, integrantes, albumes);
    }

    private ArtistaDTO crearTheWeeknd(List<PersonaDTO> personas) {
        String genero = idGeneros.get("RNB");
        String persona = nuevaPersona(personas, "Abel Tesfaye", "Cantante y compositor canadiense, conocido como The Weeknd.", "imagenes/personas/abel_tesfaye.jpg");

        List<IntegranteDTO> integrantes = List.of(
                integrante(persona, "Solista", LocalDate.of(2010, 1, 1), null, true)
        );

        List<AlbumDTO> albumes = List.of(
                album("After Hours", "2020-03-20", genero, "imagenes/albumes/after_hours.jpg",
                        cancion("Blinding Lights", "03:20", genero),
                        cancion("Save Your Tears", "03:35", genero)),
                album("Dawn FM", "2022-01-07", genero, "imagenes/albumes/dawn_fm.jpg",
                        cancion("Sacrifice", "03:08", genero),
                        cancion("Take My Breath", "03:44", genero))
        );

        return new ArtistaDTO("The Weeknd", "imagenes/solistas/the_weeknd.jpg", genero, integrantes, albumes);
    }
}