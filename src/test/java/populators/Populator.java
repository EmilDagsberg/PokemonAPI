package populators;

import app.daos.LocationDAO;
import app.daos.PokedexDAO;
import app.daos.PokemonDAO;
import app.dtos.PokemonDTO;
import app.security.daos.SecurityDAO;
import app.security.entities.User;
import dk.bugelhartmann.UserDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Populator {

    private static PokedexDAO pokedexDAO;
    private static PokemonDAO pokemonDAO;
    private static LocationDAO locationDAO;
    private static SecurityDAO securityDAO;
    private static EntityManagerFactory emf;

    public Populator(PokedexDAO pokedexDAO, PokemonDAO pokemonDAO, LocationDAO locationDAO, SecurityDAO securityDAO, EntityManagerFactory emf) {
        this.pokedexDAO = pokedexDAO;
        this.pokemonDAO = pokemonDAO;
        this.locationDAO = locationDAO;
        this.securityDAO = securityDAO;
        this.emf = emf;
    }

    public List<PokemonDTO> populatePokemonAndLocations() {
        List<PokemonDTO> pokemonDTOList = pokemonDAO.populate();
        return pokemonDTOList;
    }

    public List<User> populateUsers() {
        User u1, u2;

        u1 = new User("user1", "password1");
        u2 = new User("user2", "password2");

        u1 = securityDAO.createUser(u1.getUsername(), u1.getPassword());
        u2 = securityDAO.createUser(u2.getUsername(), u2.getPassword());

        return new ArrayList<>(Arrays.asList(u1, u2));
    }

    public void cleanUpDb() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Pokemon").executeUpdate();
            em.createQuery("DELETE FROM Location").executeUpdate();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createQuery("DELETE FROM Pokedex").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE location_id_seq RESTART WITH 1;").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
