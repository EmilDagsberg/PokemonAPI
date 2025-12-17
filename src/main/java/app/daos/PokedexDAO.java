package app.daos;

import app.config.HibernateConfig;
import app.dtos.PokedexEntryDTO;
import app.dtos.PokemonDTO;
import app.entities.Pokedex;
import app.entities.PokedexId;
import app.entities.Pokemon;
import app.exceptions.ApiException;
import app.security.entities.User;
import dk.bugelhartmann.UserDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class PokedexDAO implements IDAO <PokedexId, Integer> {

    private static PokedexDAO instance;
    private static EntityManagerFactory emf;
    private static PokemonDAO pokemonDAO = new PokemonDAO();

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

    public List<PokedexEntryDTO> getUserPokedex(String username) {
        try (EntityManager em = emf.createEntityManager()) {

            // Step 1: Get pokedex entries for this user
            TypedQuery<Pokedex> query = em.createQuery(
                    "SELECT pd FROM Pokedex pd " +
                            "WHERE pd.user.username = :username",
                    Pokedex.class);

            query.setParameter("username", username);
            List<Pokedex> pokedexEntries = query.getResultList();

            // Step 2: For each entry, get the full Pokemon and create DTO
            return pokedexEntries.stream()
                    .map(pokedexEntry -> {
                        // Get the full Pokemon with locations/types
                        PokemonDTO pokemonDTO = pokemonDAO.getById(pokedexEntry.getPokemon().getId());

                        // Create DTO with Pokemon data
                        PokedexEntryDTO dto = new PokedexEntryDTO(pokemonDTO);
                        dto.setOnTeam(pokedexEntry.isOnTeam()); // Add the onTeam status

                        return dto;
                    })
                    .collect(Collectors.toList());

        } catch (NoResultException e) {
            return Collections.emptyList();
        }
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
