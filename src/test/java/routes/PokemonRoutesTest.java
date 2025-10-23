package routes;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.daos.LocationDAO;
import app.daos.PokedexDAO;
import app.daos.PokemonDAO;
import app.dtos.PokemonDTO;
import app.dtos.TypeSlot;
import app.entities.Location;
import app.entities.Pokemon;
import app.security.daos.SecurityDAO;
import app.security.entities.User;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import populators.Populator;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PokemonRoutesTest {

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
    void getPokemonById() {
        PokemonDTO pokemon =
                given()
                        .when()
                        .get(BASE_URL + "/pokemon/" + pokemonList.get(0).getId())
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().as(PokemonDTO.class);

        assertThat(pokemon.getName(), equalTo(pokemonList.get(0).getName()));
        assertThat(pokemon.getId(), equalTo(pokemonList.get(0).getId()));

    }

    @Test
    void getAllPokemon() {
        PokemonDTO[] pokemon =
                given()
                        .when()
                        .get(BASE_URL + "/pokemon")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().as(PokemonDTO[].class);
        assertEquals(151, pokemon.length);
        assertThat(pokemon[130].getName(), equalTo(pokemonList.get(130).getName()));
    }

    @Test
    void updatePokemon() {
        PokemonDTO updatedData = new PokemonDTO(new Pokemon(130, "updated", "grass", Set.of(new Location("vej"))));

        PokemonDTO updatedPokemon =
                given()
                        .contentType("application/json")
                        .body(updatedData)
                        .when()
                        .put(BASE_URL + "/pokemon/" + updatedData.getId())
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .as(PokemonDTO.class);

        assertThat(updatedPokemon.getName(), equalTo(updatedData.getName()));
        assertThat(updatedPokemon.getId(), equalTo(updatedData.getId()));

    }

    @Test
    void createPokemon() {
        PokemonDTO newPokemon = new PokemonDTO(new Pokemon(155, "Cyndaquil", "fire", Set.of(new Location("vej"))));

        PokemonDTO pokemonCreated = given()
                .contentType("application/json")
                .body(newPokemon)
                .when()
                .post(BASE_URL + "/pokemon")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(PokemonDTO.class);

        PokemonDTO[] pokemon =
                given()
                        .when()
                        .get(BASE_URL + "/pokemon")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().as(PokemonDTO[].class);

        assertThat(pokemonCreated.getName(), equalTo("Cyndaquil"));
        assertTrue(Arrays.stream(pokemon)
                .anyMatch(p -> p.getName().equals("Cyndaquil")));

        assertThat(pokemon.length, equalTo(152));
    }

    @Test
    void deletePokemon() {
        given()
                .when()
                .delete(BASE_URL + "/pokemon/" + pokemonList.get(0).getId())
                .then()
                .log().all()
                .statusCode(204);

        PokemonDTO[] pokemon =
                given()
                        .when()
                        .get(BASE_URL + "/pokemon")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .as(PokemonDTO[].class);

        assertThat(pokemon.length, equalTo(150));

    }

    @Test
    void getPokemonByType_Fire() {
        PokemonDTO[] firePokemons = given()
                .when()
                .get(BASE_URL + "/pokemon/type/fire")
                .then()
                .statusCode(200)
                .extract()
                .as(PokemonDTO[].class);

        assertThat(firePokemons.length, greaterThan(0));

        for (PokemonDTO p : firePokemons) {
            assertThat(p.getFirstTypeName(), equalTo("fire"));
        }

        List<String> expectedFireNames = pokemonList.stream()
                .filter(p -> "fire".equalsIgnoreCase(p.getFirstTypeName()))
                .map(PokemonDTO::getName)
                .toList();

        List<String> actualFireNames = Arrays.stream(firePokemons)
                .map(PokemonDTO::getName)
                .toList();

        assertThat(actualFireNames, containsInAnyOrder(expectedFireNames.toArray()));
    }



}
