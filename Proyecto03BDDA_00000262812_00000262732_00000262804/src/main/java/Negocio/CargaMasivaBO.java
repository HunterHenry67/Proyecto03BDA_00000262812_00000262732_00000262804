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

        artists.add(crearSolista(personas, "Billie Eilish", "Cantante y compositora estadounidense.", "imagenes/personas/billie_eilish.jpg", "imagenes/solistas/billie_eilish.jpg", pop, LocalDate.of(2015, 1, 1),
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
                album("Nadie Sabe Lo Que Va a Pasar Mañana", "2023-10-13", latino, "imagenes/albumes/nadie_sabe_lo_que_va_a_pasar_manana.jpg", cancion("Monaco", "03:12", latino), cancion("VOU 787", "03:31", latino))
            )
        ));

        artists.add(crearSolista(personas, "Shakira", "Cantante y compositora colombiana.", "imagenes/personas/shakira.jpg", "imagenes/solistas/shakira.jpg", latino, LocalDate.of(1995, 1, 1),
            List.of(
                album("El Dorado", "2017-05-26", latino, "imagenes/albumes/el_dorado.jpg", cancion("Chantaje", "03:21", latino), cancion("Perro Fiel", "03:24", latino)),
                album("Las Mujeres Ya No Lloran", "2024-03-22", latino, "imagenes/albumes/las_mujeres_ya_no_lloran.jpg", cancion("Puntería", "03:01", latino), cancion("Te Felicito", "02:52", latino))
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

        artists.add(crearSolista(personas, "Rihanna", "Cantante y empresaria barbadense.", "imagenes/personas/rihanna.jpg", "imagenes/solistas/rihanna.jpg", rnb, LocalDate.of(2005, 1, 1),
            List.of(
                album("Good Girl Gone Bad", "2007-05-31", rnb, "imagenes/albumes/good_girl_gone_bad.jpg", cancion("Umbrella", "04:35", rnb), cancion("Don't Stop the Music", "04:27", rnb)),
                album("Anti", "2016-01-28", rnb, "imagenes/albumes/anti.jpg", cancion("Work", "03:39", rnb), cancion("Needed Me", "03:11", rnb))
            )
        ));

        artists.add(crearSolista(personas, "Ariana Grande", "Cantante y compositora estadounidense.", "imagenes/personas/ariana_grande.jpg", "imagenes/solistas/ariana_grande.jpg", pop, LocalDate.of(2011, 1, 1),
            List.of(
                album("Sweetener", "2018-08-17", pop, "imagenes/albumes/sweetener.jpg", cancion("No Tears Left to Cry", "03:25", pop), cancion("God Is a Woman", "03:17", pop)),
                album("Eternal Sunshine", "2024-03-08", pop, "imagenes/albumes/eternal_sunshine.jpg", cancion("Yes, And?", "03:35", pop), cancion("We Can't Be Friends", "03:48", pop))
            )
        ));

        artists.add(crearSolista(personas, "Ed Sheeran", "Cantante y compositor británico.", "imagenes/personas/ed_sheeran.jpg", "imagenes/solistas/ed_sheeran.jpg", pop, LocalDate.of(2011, 1, 1),
            List.of(
                album("Divide", "2017-03-03", pop, "imagenes/albumes/divide.jpg", cancion("Shape of You", "03:53", pop), cancion("Perfect", "04:23", pop)),
                album("Equals", "2021-10-29", pop, "imagenes/albumes/equals.jpg", cancion("Bad Habits", "03:51", pop), cancion("Shivers", "03:27", pop))
            )
        ));

        artists.add(crearSolista(personas, "Lady Gaga", "Cantante, compositora y actriz estadounidense.", "imagenes/personas/lady_gaga.jpg", "imagenes/solistas/lady_gaga.jpg", pop, LocalDate.of(2008, 1, 1),
            List.of(
                album("The Fame Monster", "2009-11-18", pop, "imagenes/albumes/the_fame_monster.jpg", cancion("Bad Romance", "04:54", pop), cancion("Alejandro", "04:34", pop)),
                album("Chromatica", "2020-05-29", pop, "imagenes/albumes/chromatica.jpg", cancion("Rain on Me", "03:02", pop), cancion("Stupid Love", "03:13", pop))
            )
        ));

        artists.add(crearSolista(personas, "Beyoncé", "Cantante, compositora y productora estadounidense.", "imagenes/personas/beyonce.jpg", "imagenes/solistas/beyonce.jpg", rnb, LocalDate.of(2003, 1, 1),
            List.of(
                album("Lemonade", "2016-04-23", rnb, "imagenes/albumes/lemonade.jpg", cancion("Formation", "03:26", rnb), cancion("Hold Up", "03:41", rnb)),
                album("Renaissance", "2022-07-29", rnb, "imagenes/albumes/renaissance.jpg", cancion("Break My Soul", "04:38", rnb), cancion("Cuff It", "03:45", rnb))
            )
        ));

        artists.add(crearBanda(personas, "Red Hot Chili Peppers", "imagenes/bandas/red_hot_chili_peppers.jpg", rock,
            List.of(
                integrante(nuevaPersona(personas, "Anthony Kiedis", "Vocalista de Red Hot Chili Peppers.", "imagenes/integrantes/anthony_kiedis.jpg"), "Vocalista", LocalDate.of(1983, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Flea", "Bajista de Red Hot Chili Peppers.", "imagenes/integrantes/flea.jpg"), "Bajista", LocalDate.of(1983, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Chad Smith", "Batería de Red Hot Chili Peppers.", "imagenes/integrantes/chad_smith.jpg"), "Batería", LocalDate.of(1988, 12, 1), null, true),
                integrante(nuevaPersona(personas, "John Frusciante", "Guitarrista de Red Hot Chili Peppers.", "imagenes/integrantes/john_frusciante.jpg"), "Guitarrista", LocalDate.of(2019, 12, 15), null, true)
            ),
            List.of(
                album("Californication", "1999-06-08", rock, "imagenes/albumes/californication.jpg", cancion("Californication", "05:29", rock), cancion("Scar Tissue", "03:35", rock)),
                album("By the Way", "2002-07-09", rock, "imagenes/albumes/by_the_way.jpg", cancion("By the Way", "03:36", rock), cancion("Can't Stop", "04:29", rock))
            )
        ));

        artists.add(crearBanda(personas, "Nirvana", "imagenes/bandas/nirvana.jpg", rock,
            List.of(
                integrante(nuevaPersona(personas, "Kurt Cobain", "Vocalista y guitarrista de Nirvana.", "imagenes/integrantes/kurt_cobain.jpg"), "Vocalista / Guitarrista", LocalDate.of(1987, 1, 1), LocalDate.of(1994, 4, 5), false),
                integrante(nuevaPersona(personas, "Krist Novoselic", "Bajista de Nirvana.", "imagenes/integrantes/krist_novoselic.jpg"), "Bajista", LocalDate.of(1987, 1, 1), LocalDate.of(1994, 4, 5), false),
                integrante(nuevaPersona(personas, "Dave Grohl", "Batería de Nirvana.", "imagenes/integrantes/dave_grohl.jpg"), "Batería", LocalDate.of(1990, 9, 1), LocalDate.of(1994, 4, 5), false)
            ),
            List.of(
                album("Nevermind", "1991-09-24", rock, "imagenes/albumes/nevermind.jpg", cancion("Smells Like Teen Spirit", "05:01", rock), cancion("Come as You Are", "03:39", rock)),
                album("In Utero", "1993-09-21", rock, "imagenes/albumes/in_utero.jpg", cancion("Heart-Shaped Box", "04:41", rock), cancion("All Apologies", "03:50", rock))
            )
        ));

        artists.add(crearBanda(personas, "Green Day", "imagenes/bandas/green_day.jpg", rock,
            List.of(
                integrante(nuevaPersona(personas, "Billie Joe Armstrong", "Vocalista y guitarrista de Green Day.", "imagenes/integrantes/billie_joe_armstrong.jpg"), "Vocalista / Guitarrista", LocalDate.of(1987, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Mike Dirnt", "Bajista de Green Day.", "imagenes/integrantes/mike_dirnt.jpg"), "Bajista", LocalDate.of(1987, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Tré Cool", "Batería de Green Day.", "imagenes/integrantes/tre_cool.jpg"), "Batería", LocalDate.of(1990, 1, 1), null, true)
            ),
            List.of(
                album("Dookie", "1994-02-01", rock, "imagenes/albumes/dookie.jpg", cancion("Basket Case", "03:01", rock), cancion("When I Come Around", "02:58", rock)),
                album("American Idiot", "2004-09-21", rock, "imagenes/albumes/american_idiot.jpg", cancion("American Idiot", "02:54", rock), cancion("Boulevard of Broken Dreams", "04:20", rock))
            )
        ));

        artists.add(crearBanda(personas, "Paramore", "imagenes/bandas/paramore.jpg", rock,
            List.of(
                integrante(nuevaPersona(personas, "Hayley Williams", "Vocalista de Paramore.", "imagenes/integrantes/hayley_williams.jpg"), "Vocalista", LocalDate.of(2004, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Taylor York", "Guitarrista de Paramore.", "imagenes/integrantes/taylor_york.jpg"), "Guitarrista", LocalDate.of(2009, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Zac Farro", "Batería de Paramore.", "imagenes/integrantes/zac_farro.jpg"), "Batería", LocalDate.of(2017, 1, 1), null, true)
            ),
            List.of(
                album("Riot!", "2007-06-12", rock, "imagenes/albumes/riot.jpg", cancion("Misery Business", "03:31", rock), cancion("That's What You Get", "03:40", rock)),
                album("After Laughter", "2017-05-12", rock, "imagenes/albumes/after_laughter.jpg", cancion("Hard Times", "03:02", rock), cancion("Rose-Colored Boy", "03:33", rock))
            )
        ));

        artists.add(crearBanda(personas, "Twenty One Pilots", "imagenes/bandas/twenty_one_pilots.jpg", rock,
            List.of(
                integrante(nuevaPersona(personas, "Tyler Joseph", "Vocalista y multiinstrumentista de Twenty One Pilots.", "imagenes/integrantes/tyler_joseph.jpg"), "Vocalista / Piano", LocalDate.of(2009, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Josh Dun", "Batería de Twenty One Pilots.", "imagenes/integrantes/josh_dun.jpg"), "Batería", LocalDate.of(2011, 1, 1), null, true)
            ),
            List.of(
                album("Blurryface", "2015-05-17", rock, "imagenes/albumes/blurryface.jpg", cancion("Stressed Out", "03:22", rock), cancion("Ride", "03:34", rock)),
                album("Trench", "2018-10-05", rock, "imagenes/albumes/trench.jpg", cancion("Jumpsuit", "03:58", rock), cancion("Chlorine", "05:24", rock))
            )
        ));

        artists.add(crearSolista(personas, "Harry Styles", "Cantante y compositor británico.", "imagenes/personas/harry_styles.jpg", "imagenes/solistas/harry_styles.jpg", pop, LocalDate.of(2017, 1, 1),
            List.of(
                album("Fine Line", "2019-12-13", pop, "imagenes/albumes/fine_line.jpg", cancion("Watermelon Sugar", "02:54", pop), cancion("Adore You", "03:27", pop)),
                album("Harry's House", "2022-05-20", pop, "imagenes/albumes/harrys_house.jpg", cancion("As It Was", "02:47", pop), cancion("Late Night Talking", "02:58", pop))
            )
        ));

        artists.add(crearSolista(personas, "Justin Bieber", "Cantante y compositor canadiense.", "imagenes/personas/justin_bieber.jpg", "imagenes/solistas/justin_bieber.jpg", pop, LocalDate.of(2009, 1, 1),
            List.of(
                album("Purpose", "2015-11-13", pop, "imagenes/albumes/purpose.jpg", cancion("Sorry", "03:20", pop), cancion("Love Yourself", "03:53", pop)),
                album("Justice", "2021-03-19", pop, "imagenes/albumes/justice.jpg", cancion("Peaches", "03:18", pop), cancion("Hold On", "02:50", pop))
            )
        ));

        artists.add(crearSolista(personas, "Miley Cyrus", "Cantante, compositora y actriz estadounidense.", "imagenes/personas/miley_cyrus.jpg", "imagenes/solistas/miley_cyrus.jpg", pop, LocalDate.of(2007, 1, 1),
            List.of(
                album("Bangerz", "2013-10-04", pop, "imagenes/albumes/bangerz.jpg", cancion("Wrecking Ball", "03:41", pop), cancion("We Can't Stop", "03:50", pop)),
                album("Endless Summer Vacation", "2023-03-10", pop, "imagenes/albumes/endless_summer_vacation.jpg", cancion("Flowers", "03:20", pop), cancion("River", "02:43", pop))
            )
        ));

        artists.add(crearSolista(personas, "Olivia Rodrigo", "Cantante, compositora y actriz estadounidense.", "imagenes/personas/olivia_rodrigo.jpg", "imagenes/solistas/olivia_rodrigo.jpg", pop, LocalDate.of(2021, 1, 1),
            List.of(
                album("Sour", "2021-05-21", pop, "imagenes/albumes/sour.jpg", cancion("Drivers License", "04:02", pop), cancion("Good 4 U", "02:58", pop)),
                album("Guts", "2023-09-08", pop, "imagenes/albumes/guts.jpg", cancion("Vampire", "03:39", pop), cancion("Bad Idea Right?", "03:04", pop))
            )
        ));

        artists.add(crearSolista(personas, "Lana Del Rey", "Cantante y compositora estadounidense.", "imagenes/personas/lana_del_rey.jpg", "imagenes/solistas/lana_del_rey.jpg", pop, LocalDate.of(2011, 1, 1),
            List.of(
                album("Born to Die", "2012-01-27", pop, "imagenes/albumes/born_to_die.jpg", cancion("Video Games", "04:42", pop), cancion("Summertime Sadness", "04:25", pop)),
                album("Ultraviolence", "2014-06-13", pop, "imagenes/albumes/ultraviolence.jpg", cancion("West Coast", "04:16", pop), cancion("Ultraviolence", "04:11", pop))
            )
        ));

        artists.add(crearBanda(personas, "The Chainsmokers", "imagenes/bandas/the_chainsmokers.jpg", electro,
            List.of(
                integrante(nuevaPersona(personas, "Alex Pall", "DJ y productor de The Chainsmokers.", "imagenes/integrantes/alex_pall.jpg"), "DJ / Productor", LocalDate.of(2012, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Drew Taggart", "DJ, productor y vocalista de The Chainsmokers.", "imagenes/integrantes/drew_taggart.jpg"), "DJ / Vocalista", LocalDate.of(2012, 1, 1), null, true)
            ),
            List.of(
                album("Memories... Do Not Open", "2017-04-07", electro, "imagenes/albumes/memories_do_not_open.jpg", cancion("Something Just Like This", "04:07", electro), cancion("Paris", "03:41", electro)),
                album("So Far So Good", "2022-05-13", electro, "imagenes/albumes/so_far_so_good.jpg", cancion("High", "03:00", electro), cancion("I Love U", "03:05", electro))
            )
        ));

        artists.add(crearBanda(personas, "Muse", "imagenes/bandas/muse.jpg", rock,
            List.of(
                integrante(nuevaPersona(personas, "Matt Bellamy", "Vocalista y guitarrista de Muse.", "imagenes/integrantes/matt_bellamy.jpg"), "Vocalista / Guitarrista", LocalDate.of(1994, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Chris Wolstenholme", "Bajista de Muse.", "imagenes/integrantes/chris_wolstenholme.jpg"), "Bajista", LocalDate.of(1994, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Dominic Howard", "Batería de Muse.", "imagenes/integrantes/dominic_howard.jpg"), "Batería", LocalDate.of(1994, 1, 1), null, true)
            ),
            List.of(
                album("Absolution", "2003-09-15", rock, "imagenes/albumes/absolution.jpg", cancion("Time Is Running Out", "03:56", rock), cancion("Hysteria", "03:47", rock)),
                album("Black Holes and Revelations", "2006-07-03", rock, "imagenes/albumes/black_holes_and_revelations.jpg", cancion("Starlight", "04:00", rock), cancion("Supermassive Black Hole", "03:29", rock))
            )
        ));

        artists.add(crearBanda(personas, "The Killers", "imagenes/bandas/the_killers.jpg", rock,
            List.of(
                integrante(nuevaPersona(personas, "Brandon Flowers", "Vocalista y tecladista de The Killers.", "imagenes/integrantes/brandon_flowers.jpg"), "Vocalista / Teclados", LocalDate.of(2001, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Dave Keuning", "Guitarrista de The Killers.", "imagenes/integrantes/dave_keuning.jpg"), "Guitarrista", LocalDate.of(2001, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Mark Stoermer", "Bajista de The Killers.", "imagenes/integrantes/mark_stoermer.jpg"), "Bajista", LocalDate.of(2002, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Ronnie Vannucci Jr.", "Batería de The Killers.", "imagenes/integrantes/ronnie_vannucci_jr.jpg"), "Batería", LocalDate.of(2002, 1, 1), null, true)
            ),
            List.of(
                album("Hot Fuss", "2004-06-07", rock, "imagenes/albumes/hot_fuss.jpg", cancion("Mr. Brightside", "03:42", rock), cancion("Somebody Told Me", "03:17", rock)),
                album("Sam's Town", "2006-10-02", rock, "imagenes/albumes/sams_town.jpg", cancion("When You Were Young", "03:40", rock), cancion("Read My Mind", "04:06", rock))
            )
        ));

        artists.add(crearBanda(personas, "Maná", "imagenes/bandas/mana.jpg", latino,
            List.of(
                integrante(nuevaPersona(personas, "Fher Olvera", "Vocalista y guitarrista de Maná.", "imagenes/integrantes/fher_olvera.jpg"), "Vocalista / Guitarrista", LocalDate.of(1986, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Juan Calleros", "Bajista de Maná.", "imagenes/integrantes/juan_calleros.jpg"), "Bajista", LocalDate.of(1986, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Álex González", "Batería de Maná.", "imagenes/integrantes/alex_gonzalez.jpg"), "Batería", LocalDate.of(1984, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Sergio Vallín", "Guitarrista de Maná.", "imagenes/integrantes/sergio_vallin.jpg"), "Guitarrista", LocalDate.of(1994, 1, 1), null, true)
            ),
            List.of(
                album("¿Dónde Jugarán los Niños?", "1992-10-27", latino, "imagenes/albumes/donde_jugaran_los_ninos.jpg", cancion("Oye Mi Amor", "04:22", latino), cancion("Vivir Sin Aire", "04:51", latino)),
                album("Sueños Líquidos", "1997-10-14", latino, "imagenes/albumes/suenos_liquidos.jpg", cancion("Clavado en un Bar", "05:11", latino), cancion("En el Muelle de San Blas", "05:59", latino))
            )
        ));

        artists.add(crearBanda(personas, "Soda Stereo", "imagenes/bandas/soda_stereo.jpg", latino,
            List.of(
                integrante(nuevaPersona(personas, "Gustavo Cerati", "Vocalista y guitarrista de Soda Stereo.", "imagenes/integrantes/gustavo_cerati.jpg"), "Vocalista / Guitarrista", LocalDate.of(1982, 1, 1), LocalDate.of(1997, 9, 20), false),
                integrante(nuevaPersona(personas, "Zeta Bosio", "Bajista de Soda Stereo.", "imagenes/integrantes/zeta_bosio.jpg"), "Bajista", LocalDate.of(1982, 1, 1), LocalDate.of(1997, 9, 20), false),
                integrante(nuevaPersona(personas, "Charly Alberti", "Batería de Soda Stereo.", "imagenes/integrantes/charly_alberti.jpg"), "Batería", LocalDate.of(1982, 1, 1), LocalDate.of(1997, 9, 20), false)
            ),
            List.of(
                album("Signos", "1986-11-10", latino, "imagenes/albumes/signos.jpg", cancion("Signos", "05:14", latino), cancion("Persiana Americana", "04:50", latino)),
                album("Canción Animal", "1990-08-07", latino, "imagenes/albumes/cancion_animal.jpg", cancion("De Música Ligera", "03:33", latino), cancion("Entre Caníbales", "04:10", latino))
            )
        ));

        artists.add(crearSolista(personas, "Karol G", "Cantante y compositora colombiana.", "imagenes/personas/karol_g.jpg", "imagenes/solistas/karol_g.jpg", latino, LocalDate.of(2013, 1, 1),
            List.of(
                album("KG0516", "2021-03-25", latino, "imagenes/albumes/kg0516.jpg", cancion("Bichota", "02:58", latino), cancion("Location", "04:25", latino)),
                album("Mañana Será Bonito", "2023-02-24", latino, "imagenes/albumes/manana_sera_bonito.jpg", cancion("TQG", "03:17", latino), cancion("Mientras Me Curo del Cora", "02:44", latino))
            )
        ));

        artists.add(crearSolista(personas, "J Balvin", "Cantante y compositor colombiano.", "imagenes/personas/j_balvin.jpg", "imagenes/solistas/j_balvin.jpg", latino, LocalDate.of(2009, 1, 1),
            List.of(
                album("Vibras", "2018-05-25", latino, "imagenes/albumes/vibras.jpg", cancion("Mi Gente", "03:06", latino), cancion("Ambiente", "04:09", latino)),
                album("Colores", "2020-03-19", latino, "imagenes/albumes/colores.jpg", cancion("Rojo", "02:31", latino), cancion("Amarillo", "02:37", latino))
            )
        ));

        artists.add(crearSolista(personas, "Rosalía", "Cantante, compositora y productora española.", "imagenes/personas/rosalia.jpg", "imagenes/solistas/rosalia.jpg", latino, LocalDate.of(2017, 1, 1),
            List.of(
                album("El Mal Querer", "2018-11-02", latino, "imagenes/albumes/el_mal_querer.jpg", cancion("Malamente", "02:30", latino), cancion("Pienso en Tu Mirá", "03:13", latino)),
                album("Motomami", "2022-03-18", latino, "imagenes/albumes/motomami.jpg", cancion("Saoko", "02:17", latino), cancion("La Fama", "03:08", latino))
            )
        ));

        artists.add(crearSolista(personas, "Post Malone", "Cantante, rapero y compositor estadounidense.", "imagenes/personas/post_malone.jpg", "imagenes/solistas/post_malone.jpg", rnb, LocalDate.of(2015, 1, 1),
            List.of(
                album("Beerbongs & Bentleys", "2018-04-27", rnb, "imagenes/albumes/beerbongs_and_bentleys.jpg", cancion("Rockstar", "03:38", rnb), cancion("Better Now", "03:51", rnb)),
                album("Hollywood's Bleeding", "2019-09-06", rnb, "imagenes/albumes/hollywoods_bleeding.jpg", cancion("Circles", "03:35", rnb), cancion("Sunflower", "02:38", rnb))
            )
        ));

        artists.add(crearSolista(personas, "SZA", "Cantante y compositora estadounidense.", "imagenes/personas/sza.jpg", "imagenes/solistas/sza.jpg", rnb, LocalDate.of(2012, 1, 1),
            List.of(
                album("Ctrl", "2017-06-09", rnb, "imagenes/albumes/ctrl.jpg", cancion("Love Galore", "04:35", rnb), cancion("The Weekend", "04:32", rnb)),
                album("SOS", "2022-12-09", rnb, "imagenes/albumes/sos.jpg", cancion("Kill Bill", "02:33", rnb), cancion("Snooze", "03:21", rnb))
            )
        ));

        artists.add(crearBanda(personas, "Zoé", "imagenes/bandas/zoe.jpg", latino,
            List.of(
                integrante(nuevaPersona(personas, "León Larregui", "Vocalista de Zoé.", "imagenes/integrantes/leon_larregui.jpg"), "Vocalista", LocalDate.of(1994, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Sergio Acosta", "Guitarrista de Zoé.", "imagenes/integrantes/sergio_acosta.jpg"), "Guitarrista", LocalDate.of(1994, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Ángel Mosqueda", "Bajista de Zoé.", "imagenes/integrantes/angel_mosqueda.jpg"), "Bajista", LocalDate.of(2001, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Jesús Báez", "Tecladista de Zoé.", "imagenes/integrantes/jesus_baez.jpg"), "Teclados", LocalDate.of(1994, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Rodrigo Guardiola", "Batería de Zoé.", "imagenes/integrantes/rodrigo_guardiola.jpg"), "Batería", LocalDate.of(2005, 1, 1), null, true)
            ),
            List.of(
                album("Reptilectric", "2008-10-08", latino, "imagenes/albumes/reptilectric.jpg", cancion("Reptilectric", "04:12", latino), cancion("Nada", "04:37", latino)),
                album("Aztlán", "2018-04-20", latino, "imagenes/albumes/aztlan.jpg", cancion("Azul", "03:27", latino), cancion("No Hay Mal Que Dure", "03:44", latino))
            )
        ));

        artists.add(crearBanda(personas, "BLACKPINK", "imagenes/bandas/blackpink.jpg", pop,
            List.of(
                integrante(nuevaPersona(personas, "Jisoo", "Vocalista de BLACKPINK.", "imagenes/integrantes/jisoo.jpg"), "Vocalista", LocalDate.of(2016, 8, 8), null, true),
                integrante(nuevaPersona(personas, "Jennie", "Rapera y vocalista de BLACKPINK.", "imagenes/integrantes/jennie.jpg"), "Rapera / Vocalista", LocalDate.of(2016, 8, 8), null, true),
                integrante(nuevaPersona(personas, "Rosé", "Vocalista de BLACKPINK.", "imagenes/integrantes/rose.jpg"), "Vocalista", LocalDate.of(2016, 8, 8), null, true),
                integrante(nuevaPersona(personas, "Lisa", "Rapera y bailarina de BLACKPINK.", "imagenes/integrantes/lisa.jpg"), "Rapera / Bailarina", LocalDate.of(2016, 8, 8), null, true)
            ),
            List.of(
                album("The Album", "2020-10-02", pop, "imagenes/albumes/the_album_blackpink.jpg", cancion("Lovesick Girls", "03:12", pop), cancion("How You Like That", "03:01", pop)),
                album("Born Pink", "2022-09-16", pop, "imagenes/albumes/born_pink.jpg", cancion("Pink Venom", "03:07", pop), cancion("Shut Down", "02:56", pop))
            )
        ));

        artists.add(crearBanda(personas, "ABBA", "imagenes/bandas/abba.jpg", pop,
            List.of(
                integrante(nuevaPersona(personas, "Agnetha Fältskog", "Vocalista de ABBA.", "imagenes/integrantes/agnetha_faltskog.jpg"), "Vocalista", LocalDate.of(1972, 1, 1), LocalDate.of(1982, 12, 31), false),
                integrante(nuevaPersona(personas, "Björn Ulvaeus", "Vocalista y guitarrista de ABBA.", "imagenes/integrantes/bjorn_ulvaeus.jpg"), "Vocalista / Guitarrista", LocalDate.of(1972, 1, 1), LocalDate.of(1982, 12, 31), false),
                integrante(nuevaPersona(personas, "Benny Andersson", "Tecladista y vocalista de ABBA.", "imagenes/integrantes/benny_andersson.jpg"), "Teclados / Vocalista", LocalDate.of(1972, 1, 1), LocalDate.of(1982, 12, 31), false),
                integrante(nuevaPersona(personas, "Anni-Frid Lyngstad", "Vocalista de ABBA.", "imagenes/integrantes/anni_frid_lyngstad.jpg"), "Vocalista", LocalDate.of(1972, 1, 1), LocalDate.of(1982, 12, 31), false)
            ),
            List.of(
                album("Arrival", "1976-10-11", pop, "imagenes/albumes/arrival.jpg", cancion("Dancing Queen", "03:51", pop), cancion("Money, Money, Money", "03:06", pop)),
                album("The Visitors", "1981-11-30", pop, "imagenes/albumes/the_visitors.jpg", cancion("One of Us", "03:56", pop), cancion("The Visitors", "05:47", pop))
            )
        ));

        artists.add(crearBanda(personas, "The Black Keys", "imagenes/bandas/the_black_keys.jpg", rock,
            List.of(
                integrante(nuevaPersona(personas, "Dan Auerbach", "Vocalista y guitarrista de The Black Keys.", "imagenes/integrantes/dan_auerbach.jpg"), "Vocalista / Guitarrista", LocalDate.of(2001, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Patrick Carney", "Batería de The Black Keys.", "imagenes/integrantes/patrick_carney.jpg"), "Batería", LocalDate.of(2001, 1, 1), null, true)
            ),
            List.of(
                album("Brothers", "2010-05-18", rock, "imagenes/albumes/brothers.jpg", cancion("Tighten Up", "03:31", rock), cancion("Howlin' for You", "03:11", rock)),
                album("El Camino", "2011-12-06", rock, "imagenes/albumes/el_camino.jpg", cancion("Lonely Boy", "03:13", rock), cancion("Gold on the Ceiling", "03:44", rock))
            )
        ));

        artists.add(crearBanda(personas, "System of a Down", "imagenes/bandas/system_of_a_down.jpg", rock,
            List.of(
                integrante(nuevaPersona(personas, "Serj Tankian", "Vocalista de System of a Down.", "imagenes/integrantes/serj_tankian.jpg"), "Vocalista", LocalDate.of(1994, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Daron Malakian", "Guitarrista y vocalista de System of a Down.", "imagenes/integrantes/daron_malakian.jpg"), "Guitarrista / Vocalista", LocalDate.of(1994, 1, 1), null, true),
                integrante(nuevaPersona(personas, "Shavo Odadjian", "Bajista de System of a Down.", "imagenes/integrantes/shavo_odadjian.jpg"), "Bajista", LocalDate.of(1994, 1, 1), null, true),
                integrante(nuevaPersona(personas, "John Dolmayan", "Batería de System of a Down.", "imagenes/integrantes/john_dolmayan.jpg"), "Batería", LocalDate.of(1997, 1, 1), null, true)
            ),
            List.of(
                album("Toxicity", "2001-09-04", rock, "imagenes/albumes/toxicity.jpg", cancion("Chop Suey!", "03:30", rock), cancion("Toxicity", "03:39", rock)),
                album("Mezmerize", "2005-05-17", rock, "imagenes/albumes/mezmerize.jpg", cancion("B.Y.O.B.", "04:15", rock), cancion("Question!", "03:20", rock))
            )
        ));

        try {
            List<PersonaDTO> personasGuardadas = personaBO.agregarMasivo(personas);
            List<ArtistaDTO> artistasGuardados = artistaBO.agregarMasivo(artists);

            return "Se agregaron " + personasGuardadas.size() + " personas/integrantes y "
                    + artistasGuardados.size() + " artistas (45 en total, con sus álbumes y canciones).";
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