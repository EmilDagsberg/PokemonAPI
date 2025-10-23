package app.daos;

import app.config.HibernateConfig;
import app.dtos.PokemonDTO;
import app.entities.Pokedex;
import app.entities.PokedexId;
import app.entities.Pokemon;
import app.exceptions.ApiException;
import app.security.entities.User;
import dk.bugelhartmann.UserDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class PokedexDAO implements IDAO <PokedexId, Integer> {

    private static PokedexDAO instance;
    private static EntityManagerFactory emf;

    private PokedexDAO() {}

    public static PokedexDAO getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new PokedexDAO();
            PokedexDAO.emf = emf;
        }
        return instance;
    }

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

    public Pokedex getPokedex (UserDTO userDTO, Integer integer){
        EntityManager em = emf.createEntityManager();

        try {
            User user = em.find(User.class, userDTO.getUsername());
            Pokemon pokemon = em.find(Pokemon.class, integer);

            if (user == null || pokemon == null) {
                throw new IllegalArgumentException("User or Pokémon not found");
            }

            PokedexId pokedexId = new PokedexId(user.getUsername(), pokemon.getId());

            Pokedex result = em.find(Pokedex.class, pokedexId);

            if (result != null){
                return result;
            }else{
                throw new IllegalArgumentException("Pokémon is not in users pokedex");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public PokedexId addPokemonToTeam(Pokedex pokedex){
        EntityManager em = emf.createEntityManager();

        try {
            Pokedex pokedexToUpdate = em.find(Pokedex.class, pokedex.getId());

            if(pokedexToUpdate != null && pokedexToUpdate.isOnTeam() == false){
                pokedex.setOnTeam(true);
            }

            em.getTransaction().begin();
            em.merge(pokedex);
            em.getTransaction().commit();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Pokedex> getPokemonsOnTeam (UserDTO userDTO){
        EntityManager em = emf.createEntityManager();

        try{
            TypedQuery query = em.createQuery("SELECT Pokedex FROM Pokedex p WHERE p.onTeam = TRUE", PokemonDTO.class);
            return query.getResultList();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PokedexId getById(Integer integer) {
        return null;
    }

    @Override
    public List<PokedexId> getAll() {
        return List.of();
    }

    @Override
    public PokedexId create(PokedexId pokedexId) {
        return null;
    }

    @Override
    public PokedexId update(PokedexId pokedexId) {
        return null;
    }

    @Override
    public boolean delete(Integer integer) {
        return false;
    }
}
