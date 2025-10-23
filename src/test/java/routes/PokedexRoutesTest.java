package routes;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.daos.LocationDAO;
import app.daos.PokedexDAO;
import app.daos.PokemonDAO;
import app.dtos.PokemonDTO;
import app.entities.Pokedex;
import app.security.daos.SecurityDAO;
import app.security.entities.User;
import dk.bugelhartmann.UserDTO;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import populators.Populator;

import java.util.List;

import static org.junit.Assert.*;

public class PokedexRoutesTest {
    private static Javalin app;
    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static String BASE_URL = "http://localhost:7070/api";
    private static PokedexDAO pokedexDAO = PokedexDAO.getInstance(emf);
    private static PokemonDAO pokemonDAO = PokemonDAO.getInstance(emf);
    private static LocationDAO locationDAO = LocationDAO.getInstance(emf);
    private static SecurityDAO securityDAO = new SecurityDAO(emf);
    private static Populator populator = new Populator(pokedexDAO, pokemonDAO, locationDAO, securityDAO, emf);

    private static User u1, u2;
    private static List<User> users;

    private static List<PokemonDTO> pokemonList;

    @BeforeAll
    static void init() {
        app = ApplicationConfig.startServer(7070);
        HibernateConfig.setTest(true);
    }

    @BeforeEach
    void setup() {
        pokemonList = populator.populatePokemonAndLocations();

        users = populator.populateUsers();
        u1 = users.get(0);
        u2 = users.get(1);
    }

    @AfterEach
    void teardown() {
        populator.cleanUpDb();
    }

    @AfterAll
    static void closeDown() {
        ApplicationConfig.stopServer(app);
    }

    @Test
    void testAddPokemonToPokedex_Success() {

        Pokedex result = pokedexDAO.addPokemonToPokedex(new UserDTO(u1.getUsername(), u1.getPassword()), 25);

        assertNotNull(result);
        assertEquals("user1", result.getId().getUsername());
        assertEquals(25, result.getId().getPokemonId());
        assertFalse(result.isOnTeam());


    }

    @Test
    void testAddPokemonToTeam_Success() {
        Pokedex pokedex = pokedexDAO.addPokemonToPokedex(new UserDTO(u1.getUsername(), u1.getPassword()), 25);

        pokedexDAO.addPokemonToTeam(pokedex);
        Pokedex result = pokedexDAO.getPokedex(new UserDTO(pokedex.getUser().getUsername(), pokedex.getUser().getPassword()), pokedex.getPokemon().getId());

        assertNotNull(result);
        assertEquals("user1", result.getId().getUsername());
        assertEquals(25, result.getId().getPokemonId());
        assertTrue(result.isOnTeam());

    }

}
