package app.daos;

import app.config.HibernateConfig;
import app.dtos.PokemonDTO;
import app.entities.Pokedex;
import app.entities.PokedexId;
import app.entities.Pokemon;
import app.security.entities.User;
import dk.bugelhartmann.UserDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class PokedexDAO implements IDAO <UserDTO, Integer> {

    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    public Pokedex addPokemonToPokedex(UserDTO userDTO, int pokemonId) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();


            User user = em.find(User.class, userDTO.getUsername());
            Pokemon pokemon = em.find(Pokemon.class, pokemonId);

            if (user == null || pokemon == null) {
                throw new IllegalArgumentException("User or Pokémon not found");
            }

            // Create the composite ID
            PokedexId id = new PokedexId(user.getUsername(), pokemon.getId());

            // Check if entry already exists
            Pokedex existing = em.find(Pokedex.class, id);
            if (existing != null) {
                throw new IllegalStateException("This Pokémon is already in the user's Pokédex");
            }

            // Create and persist new entry
            Pokedex pokedex = new Pokedex();
            pokedex.setId(id);
            pokedex.setUser(user);
            pokedex.setPokemon(pokemon);
            pokedex.setOnTeam(false);

            em.persist(pokedex);
            em.getTransaction().commit();

            return pokedex;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public UserDTO getById(Integer integer) {
        return null;
    }

    @Override
    public List<UserDTO> getAll() {
        return List.of();
    }

    @Override
    public UserDTO create(UserDTO userDTO) {
        return null;
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        return null;
    }

    @Override
    public boolean delete(Integer integer) {
        return false;
    }
}
