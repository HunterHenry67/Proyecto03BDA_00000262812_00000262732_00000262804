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
 * Carga masiva de un catálogo simplificada.
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
     * @return mensaje con el resumen de lo insertado.
     */
    public String ejecutarCargaMasiva() throws NegocioException {
        if (cargaMasivaYaRealizada()) {
            throw new NegocioException(
                    "La carga masiva ya fue realizada anteriormente."
            );
        }

        // 1. RESOLVER GÉNEROS PRIMERO (Filtro de seguridad inicial)
        this.idGeneros = resolverGeneros();
        
        String rock = idGeneros.get("ROCK");
        String pop = idGeneros.get("POP");
        String electro = idGeneros.get("ELECTRONICA");
        String rnb = idGeneros.get("RNB");
        String latino = idGeneros.get("LATINO");

        List<PersonaDTO> personas = new ArrayList<>();
        List<ArtistaDTO> artists = new ArrayList<>();

        artists.add(crearBanda(personas, "Linkin Park", "imagenes/bandas/linkin_park.jpg", rock,
            List.of(
                integrante(nuevaPersona(personas, "Chester Bennington", "Vocalista original de Linkin Park.", "imagenes/integrantes/chester_bennington.jpg"), "Vocalista", LocalDate.of(1999, 1, 1), LocalDate.of(2017, 7, 20), false),
                integrante(nuevaPersona(personas, "Mike Shinoda", "Vocalista, rapero y guitarrista de Linkin Park.", "imagenes/integrantes/mike_shinoda.jpg"), "Vocalista / Guitarrista", LocalDate.of(1996, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Brad Delson", "Guitarrista de Linkin Park.", "imagenes/integrantes/brad_delson.jpg"), "Guitarrista", LocalDate.of(1996, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Dave Farrell", "Bajista de Linkin Park.", "imagenes/integrantes/dave_farrell.jpg"), "Bajista", LocalDate.of(1996, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Joe Hahn", "DJ y turntablist de Linkin Park.", "imagenes/integrantes/joe_hahn.jpg"), "DJ / Turntables", LocalDate.of(1996, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Emily Armstrong", "Vocalista de Linkin Park desde 2024.", "imagenes/integrantes/emily_armstrong.jpg"), "Vocalista", LocalDate.of(2024, 9, 5), null, true)
            ),
            List.of(
                album("Hybrid Theory", "2000-10-24", rock, "imagenes/albumes/hybrid_theory.jpg", cancion("In the End", "03:36", rock), cancion("One Step Closer", "02:35", rock)),
                album("Meteora", "2003-03-25", rock, "imagenes/albumes/meteora.jpg", cancion("Numb", "03:07", rock), cancion("Faint", "02:42", rock))
            )
        ));

        artists.add(crearBanda(personas, "Coldplay", "imagenes/bandas/coldplay.jpg", rock,
            List.of(
                integrante(nuevaPersona(personas, "Chris Martin", "Vocalista y pianista de Coldplay.", "imagenes/integrantes/chris_martin.jpg"), "Vocalista / Piano", LocalDate.of(1996, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Jonny Buckland", "Guitarrista de Coldplay.", "imagenes/integrantes/jonny_buckland.jpg"), "Guitarrista", LocalDate.of(1996, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Guy Berryman", "Bajista de Coldplay.", "imagenes/integrantes/guy_berryman.jpg"), "Bajista", LocalDate.of(1996, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Will Champion", "Batería de Coldplay.", "imagenes/integrantes/will_champion.jpg"), "Batería", LocalDate.of(1997, 1, 1), null, true)
            ),
            List.of(
                album("Parachutes", "2000-07-10", rock, "imagenes/albumes/parachutes.jpg", cancion("Yellow", "04:29", rock), cancion("Trouble", "04:33", rock)),
                album("Music of the Spheres", "2021-10-15", rock, "imagenes/albumes/music_of_the_spheres.jpg", cancion("Higher Power", "03:29", rock), cancion("My Universe", "03:52", rock))
            )
        ));

        artists.add(crearBanda(personas, "Daft Punk", "imagenes/bandas/daft_punk.jpg", electro,
            List.of(
                integrante(nuevaPersona(personas, "Thomas Bangalter", "Productor y DJ, mitad de Daft Punk.", "imagenes/integrantes/thomas_bangalter.jpg"), "Productor / DJ", LocalDate.of(1993, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Guy-Manuel de Homem-Christo", "Productor y DJ, mitad de Daft Punk.", "imagenes/integrantes/guy_manuel.jpg"), "Productor / DJ", LocalDate.of(1993, 1, 1), null, true)
            ),
            List.of(
                album("Discovery", "2001-03-12", electro, "imagenes/albumes/discovery.jpg", cancion("One More Time", "05:20", electro), cancion("Digital Love", "04:58", electro)),
                album("Random Access Memories", "2013-05-17", electro, "imagenes/albumes/random_access_memories.jpg", cancion("Get Lucky", "04:08", electro), cancion("Instant Crush", "03:37", electro))
            )
        ));

        artists.add(crearBanda(personas, "Imagine Dragons", "imagenes/bandas/imagine_dragons.jpg", rock,
            List.of(
                integrante(nuevaPersona(personas, "Dan Reynolds", "Vocalista de Imagine Dragons.", "imagenes/integrantes/dan_reynolds.jpg"), "Vocalista", LocalDate.of(2008, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Wayne Sermon", "Guitarrista de Imagine Dragons.", "imagenes/integrantes/wayne_sermon.jpg"), "Guitarrista", LocalDate.of(2008, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Ben McKee", "Bajista de Imagine Dragons.", "imagenes/integrantes/ben_mckee.jpg"), "Bajista", LocalDate.of(2010, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Daniel Platzman", "Batería de Imagine Dragons.", "imagenes/integrantes/daniel_platzman.jpg"), "Batería", LocalDate.of(2011, 1, 1), null, true)
            ),
            List.of(
                album("Night Visions", "2012-09-04", rock, "imagenes/albumes/night_visions.jpg", cancion("Radioactive", "03:07", rock), cancion("Demons", "02:57", rock)),
                album("Evolve", "2017-06-23", rock, "imagenes/albumes/evolve.jpg", cancion("Believer", "03:24", rock), cancion("Thunder", "03:07", rock))
            )
        ));

        artists.add(crearBanda(personas, "Arctic Monkeys", "imagenes/bandas/artic_monkeys.jpg", rock,
            List.of(
                integrante(nuevaPersona(personas, "Alex Turner", "Vocalista y guitarrista de Arctic Monkeys.", "imagenes/integrantes/alex_turner.jpg"), "Vocalista / Guitarrista", LocalDate.of(2002, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Jamie Cook", "Guitarrista de Arctic Monkeys.", "imagenes/integrantes/jamie_cook.jpg"), "Guitarrista", LocalDate.of(2002, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Matt Helders", "Batería de Arctic Monkeys.", "imagenes/integrantes/matt_helders.jpg"), "Batería", LocalDate.of(2002, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Nick O'Malley", "Bajista de Arctic Monkeys.", "imagenes/integrantes/nick_omalley.jpg"), "Bajista", LocalDate.of(2006, 1, 1), null, true)
            ),
            List.of(
                album("AM", "2013-09-09", rock, "imagenes/albumes/am.jpg", cancion("Do I Wanna Know?", "04:32", rock), cancion("R U Mine?", "03:21", rock)),
                album("The Car", "2022-10-21", rock, "imagenes/albumes/the_car.jpg", cancion("There'd Better Be a Mirrorball", "04:32", rock), cancion("Body Paint", "04:20", rock))
            )
        ));

        artists.add(crearBanda(personas, "Metallica", "imagenes/bandas/metallica.jpg", rock,
            List.of(
                integrante(nuevaPersona(personas, "James Hetfield", "Vocalista y guitarrista de Metallica.", "imagenes/integrantes/james_hetfield.jpg"), "Vocalista / Guitarrista", LocalDate.of(1981, 10, 28), null, true),
                integrante(nuevaPersona(personas, "Lars Ulrich", "Batería de Metallica.", "imagenes/integrantes/lars_ulrich.jpg"), "Batería", LocalDate.of(1981, 10, 28), null, true),
                integrante(nuevaPersona(personas, "Kirk Hammett", "Guitarrista de Metallica.", "imagenes/integrantes/kirk_hammett.jpg"), "Guitarrista", LocalDate.of(1983, 4, 1), null, true),
                integrante(nuevaPersona(personas, "Robert Trujillo", "Bajista de Metallica.", "imagenes/integrantes/robert_trujillo.jpg"), "Bajista", LocalDate.of(2003, 2, 24), null, true)
            ),
            List.of(
                album("Master of Puppets", "1986-03-03", rock, "imagenes/albumes/master_of_puppets.jpg", cancion("Master of Puppets", "08:35", rock), cancion("Battery", "05:12", rock)),
                album("72 Seasons", "2023-04-14", rock, "imagenes/albumes/72_seasons.jpg", cancion("Lux Æterna", "03:19", rock), cancion("72 Seasons", "05:11", rock))
            )
        ));

        artists.add(crearBanda(personas, "Queen", "imagenes/bandas/queen.jpg", rock,
            List.of(
                integrante(nuevaPersona(personas, "Freddie Mercury", "Vocalista original de Queen.", "imagenes/integrantes/freddie_mercury.jpg"), "Vocalista", LocalDate.of(1970, 6, 27), LocalDate.of(1991, 11, 24), false),
                integrante(nuevaPersona(personas, "Brian May", "Guitarrista de Queen.", "imagenes/integrantes/brian_may.jpg"), "Guitarrista", LocalDate.of(1970, 6, 27), null, true),
                integrante(nuevaPersona(personas, "Roger Taylor", "Batería de Queen.", "imagenes/integrantes/roger_taylor.jpg"), "Batería", LocalDate.of(1970, 6, 27), null, true),
                integrante(nuevaPersona(personas, "John Deacon", "Bajista de Queen.", "imagenes/integrantes/john_deacon.jpg"), "Bajista", LocalDate.of(1971, 2, 1), LocalDate.of(1997, 1, 1), false)
            ),
            List.of(
                album("A Night at the Opera", "1975-11-21", rock, "imagenes/albumes/a_night_at_the_opera.jpg", cancion("Bohemian Rhapsody", "05:55", rock), cancion("You're My Best Friend", "02:52", rock)),
                album("The Game", "1980-06-30", rock, "imagenes/albumes/the_game.jpg", cancion("Another One Bites the Dust", "03:35", rock), cancion("Crazy Little Thing Called Love", "02:43", rock))
            )
        ));

        artists.add(crearSolista(personas, "Dua Lipa", "Cantante y compositora británica.", "imagenes/personas/dua_lipa.jpg", "imagenes/solistas/dua_lipa.jpg", pop, LocalDate.of(2015, 1, 1),
            List.of(
                album("Future Nostalgia", "2020-03-27", pop, "imagenes/albumes/future_nostalgia.jpg", cancion("Don't Start Now", "03:03", pop), cancion("Levitating", "03:23", pop)),
                album("Radical Optimism", "2024-05-03", pop, "imagenes/albumes/radical_optimism.jpg", cancion("Houdini", "03:07", pop), cancion("Training Season", "03:20", pop))
            )
        ));

        artists.add(crearBanda(personas, "Billie Eilish", "imagenes/solistas/billie_eilish.jpg", pop,
            List.of(
                integrante(nuevaPersona(personas, "Billie Eilish", "Cantante y compositora estadounidense.", "imagenes/personas/billie_eilish.jpg"), "Vocalista", LocalDate.of(2015, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Finneas O'Connell", "Productor y compositor, hermano y colaborador de Billie Eilish.", "imagenes/integrantes/finneas.jpg"), "Productor / Compositor", LocalDate.of(2015, 1, 1), null, true)
            ),
            List.of(
                album("When We All Fall Asleep, Where Do We Go?", "2019-03-29", pop, "imagenes/albumes/when_we_all_fall_asleep_where_do_we_go.jpg", cancion("Bad Guy", "03:14", pop), cancion("Bury a Friend", "03:13", pop)),
                album("Hit Me Hard and Soft", "2024-05-17", pop, "imagenes/albumes/hit_me_hard_and_soft.jpg", cancion("Birds of a Feather", "03:30", pop), cancion("Lunch", "03:20", pop))
            )
        ));

        artists.add(crearSolista(personas, "Bruno Mars", "Cantante, compositor y productor estadounidense.", "imagenes/personas/bruno_mars.jpg", "imagenes/solistas/bruno_mars.jpg", pop, LocalDate.of(2010, 1, 1),
            List.of(
                album("Doo-Wops & Hooligans", "2010-10-04", pop, "imagenes/albumes/doo_wops_and_hooligans.jpg", cancion("Just the Way You Are", "03:40", pop), cancion("Grenade", "03:42", pop)),
                album("24K Magic", "2016-11-18", pop, "imagenes/albumes/24k_magic.jpg", cancion("24K Magic", "03:46", pop), cancion("That's What I Like", "03:26", pop))
            )
        ));

        artists.add(crearSolista(personas, "Bad Bunny", "Cantante y compositor puertorriqueño de reggaetón.", "imagenes/personas/bad_bunny.jpg", "imagenes/solistas/bad_bunny.jpg", latino, LocalDate.of(2016, 1, 1),
            List.of(
                album("X100PRE", "2018-12-24", latino, "imagenes/albumes/x100pre.jpg", cancion("Estamos Bien", "03:23", latino), cancion("Callaita", "03:59", latino)),
                album("Nadie Sabe Lo Que Va a Pasar Mañana", "2023-10-13", latino, "imagenes/albumes/nadie_sabe_lo_que_va_a_pasar_manana.jpg", cancion("Monaco", "03:12", latino), cancion("Vou 787", "03:31", latino)),
                album("Las Mujeres Ya No Lloran", "2024-10-04", latino, "imagenes/albumes/las_mujeres_ya_no_lloran.jpg", cancion("Voy a Llevarte pa PR", "02:51", latino), cancion("Baticano", "04:03", latino))
            )
        ));

        artists.add(crearSolista(personas, "Shakira", "Cantante y compositora colombiana.", "imagenes/personas/shakira.jpg", "imagenes/solistas/shakira.jpg", latino, LocalDate.of(1995, 1, 1),
            List.of(
                album("El Dorado", "2017-05-26", latino, "imagenes/albumes/el_dorado.jpg", cancion("Chantaje", "03:21", latino), cancion("Perro Fiel", "03:24", latino))
            )
        ));

        artists.add(crearSolista(personas, "Taylor Swift", "Cantante y compositora estadounidense.", "imagenes/personas/taylor_swift.jpg", "imagenes/solistas/taylor_swift.jpg", pop, LocalDate.of(2006, 1, 1),
            List.of(
                album("Midnights", "2022-10-21", pop, "imagenes/albumes/midnights.jpg", cancion("Anti-Hero", "03:20", pop), cancion("Lavender Haze", "03:22", pop)),
                album("1989 (Taylor's Version)", "2023-10-27", pop, "imagenes/albumes/1989_taylors_version.jpg", cancion("Style", "03:51", pop), cancion("Blank Space", "03:51", pop))
            )
        ));

        artists.add(crearSolista(personas, "Adele", "Cantante y compositora británica.", "imagenes/personas/adele.jpg", "imagenes/solistas/adele.jpg", pop, LocalDate.of(2008, 1, 1),
            List.of(
                album("21", "2011-01-24", pop, "imagenes/albumes/21.jpg", cancion("Rolling in the Deep", "03:48", pop), cancion("Someone Like You", "04:45", pop)),
                album("30", "2021-11-19", pop, "imagenes/albumes/30.jpg", cancion("Easy on Me", "03:44", pop), cancion("Oh My God", "03:39", pop))
            )
        ));

        artists.add(crearSolista(personas, "The Weeknd", "Cantante y compositor canadiense, conocido como The Weeknd.", "imagenes/personas/abel_tesfaye.jpg", "imagenes/solistas/the_weeknd.jpg", rnb, LocalDate.of(2010, 1, 1),
            List.of(
                album("After Hours", "2020-03-20", rnb, "imagenes/albumes/after_hours.jpg", cancion("Blinding Lights", "03:20", rnb), cancion("Save Your Tears", "03:35", rnb)),
                album("Dawn FM", "2022-01-07", rnb, "imagenes/albumes/dawn_fm.jpg", cancion("Sacrifice", "03:08", rnb), cancion("Take My Breath", "03:44", rnb))
            )
        ));

      
        try {
            List<PersonaDTO> personasGuardadas = personaBO.agregarMasivo(personas);
            List<ArtistaDTO> artistasGuardados = artistaBO.agregarMasivo(artists);

            return "Se agregaron " + personasGuardadas.size() + " personas/integrantes y "
                    + artistasGuardados.size() + " artistas (con sus álbumes y canciones).";
        } catch (NegocioException ex) {
            throw new NegocioException("Error al realizar la carga masiva: " + ex.getMessage());
        }
    }


    private ArtistaDTO crearBanda(List<PersonaDTO> personas, String nombreBanda, String imagenBanda, String generoId, 
                                  List<IntegranteDTO> integrantes, List<AlbumDTO> albumes) {
        return new ArtistaDTO(nombreBanda, imagenBanda, generoId, integrantes, albumes);
    }

    private ArtistaDTO crearSolista(List<PersonaDTO> personas, String nombre, String descripcion, String imagenPersona, 
                                    String imagenArtista, String generoId, LocalDate fechaIngreso, List<AlbumDTO> albumes) {
        String idPersona = nuevaPersona(personas, nombre, descripcion, imagenPersona);
        List<IntegranteDTO> integrantes = List.of(integrante(idPersona, "Solista", fechaIngreso, null, true));
        return new ArtistaDTO(nombre, imagenArtista, generoId, integrantes, albumes);
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
        album.setCanciones(List.of(canciones));
        return album;
    }

    private boolean cargaMasivaYaRealizada() throws NegocioException {
        List<ArtistaDTO> encontrados = artistaBO.buscarPorNombre("Linkin Park");

        if (encontrados == null) {
            return false;
        }

        for (ArtistaDTO artista : encontrados) {
            if (artista != null
                    && artista.getNombre() != null
                    && artista.getNombre().equalsIgnoreCase("Linkin Park")) {
                return true;
            }
        }

        return false;
    }


    private Map<String, String> resolverGeneros() throws NegocioException {
        try {
            List<GeneroDTO> todos = generoBO.consultarTodos();

            Map<String, String> porNombre = new HashMap<>();

            if (todos != null) {
                for (GeneroDTO genero : todos) {
                    if (genero != null
                            && genero.getNombre() != null
                            && genero.getId() != null) {

                        porNombre.put(
                                normalizar(genero.getNombre()),
                                genero.getId()
                        );
                    }
                }
            }

            Map<String, String> resultado = new HashMap<>();

            resultado.put(
                    "ROCK",
                    obtenerOCrearGenero(porNombre, "Rock")
            );

            resultado.put(
                    "POP",
                    obtenerOCrearGenero(porNombre, "Pop")
            );

            resultado.put(
                    "ELECTRONICA",
                    obtenerOCrearGenero(
                            porNombre,
                            "Electrónica",
                            "Electronica",
                            "Electronic"
                    )
            );

            resultado.put(
                    "RNB",
                    obtenerOCrearGenero(
                            porNombre,
                            "R&B",
                            "RnB",
                            "R & B"
                    )
            );

            resultado.put(
                    "LATINO",
                    obtenerOCrearGenero(
                            porNombre,
                            "Reggaetón",
                            "Reggaeton",
                            "Latino",
                            "Urbano"
                    )
            );

            return resultado;

        } catch (PersistenciaException ex) {
            throw new NegocioException(
                    "No se pudieron preparar los géneros: "
                    + ex.getMessage()
            );
        }
    }

    private String obtenerOCrearGenero(
            Map<String, String> porNombre,
            String nombrePrincipal,
            String... nombresAlternativos
    ) throws PersistenciaException {

        String idEncontrado = porNombre.get(
                normalizar(nombrePrincipal)
        );

        if (idEncontrado != null) {
            return idEncontrado;
        }

        for (String nombreAlternativo : nombresAlternativos) {
            idEncontrado = porNombre.get(
                    normalizar(nombreAlternativo)
            );

            if (idEncontrado != null) {
                return idEncontrado;
            }
        }

        GeneroDTO generoNuevo = new GeneroDTO();
        generoNuevo.setNombre(nombrePrincipal);

        GeneroDTO generoGuardado = generoBO.agregar(generoNuevo);

        porNombre.put(
                normalizar(generoGuardado.getNombre()),
                generoGuardado.getId()
        );

        return generoGuardado.getId();
    }

    private String normalizar(String texto) {
        return texto == null
                ? ""
                : texto.trim().toLowerCase();
    }
}