package app.daos;

import app.config.HibernateConfig;
import app.dtos.LocationDTO;
import app.dtos.PokemonDTO;
import app.entities.Location;
import app.entities.Pokemon;
import app.services.LocationServices;
import app.services.PokemonServices;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PokemonDAO implements IDAO <PokemonDTO, Integer> {

    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    @Override
    public PokemonDTO getById(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            Pokemon pokemon = em.find(Pokemon.class, integer);
            return new PokemonDTO(pokemon);
        }
    }

    @Override
    public List<PokemonDTO> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<PokemonDTO> query = em.createQuery("SELECT new app.dtos.PokemonDTO(p) FROM Pokemon p", PokemonDTO.class);
            return query.getResultList();
        }
    }

    @Override
    public PokemonDTO create(PokemonDTO dto) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Pokemon pokemon = new Pokemon(dto);
            em.persist(pokemon);
            em.getTransaction().commit();
            em.close();
            return new PokemonDTO(pokemon);
        }
    }

    @Override
    public PokemonDTO update(PokemonDTO dto) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Pokemon pokemon = new Pokemon(dto);
            em.merge(pokemon);
            em.getTransaction().commit();
            em.close();
            return new PokemonDTO(pokemon);
        }
    }

    @Override
    public boolean delete(Integer integer) {
        EntityManager em = emf.createEntityManager();

        try {
            Pokemon hotel = em.find(Pokemon.class, integer);

            em.getTransaction().begin();
            em.remove(hotel);
            em.getTransaction().commit();
//            logger.info("Pokemon with ID: " + hotel.getId() + " has been deleted");
            // TODO: Tilføj fejlbesked. Skal Logger bruges?
            return true;
        } catch (Exception e) {
            if (em.getTransaction() != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
//            logger.info("Something went wrong. Changes have been rolled back");
            // TODO: Tilføj fejlbesked. Skal Logger bruges?
            e.printStackTrace();
            return false;
        }
    }

    public void populate() {
        PokemonServices pokemonServices = new PokemonServices();
        LocationServices locationServices = new LocationServices();

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            List<PokemonDTO> pokemonDTOS = pokemonServices.fetchFirstGenPokemon();
            Map<Integer, Pokemon> pokemonMap = new HashMap<>();


            for (PokemonDTO p : pokemonDTOS) {
                Pokemon pokemon = new Pokemon(p);
                em.persist(pokemon);
                pokemonMap.put(pokemon.getId(), pokemon);
            }

            for (PokemonDTO pokemonDTO : pokemonDTOS) {
                Pokemon pokemon = pokemonMap.get(pokemonDTO.getId());
                List<LocationDTO> locationDTOs = locationServices.fetchPokemonLocations(List.of(pokemonDTO));

                for (LocationDTO locDTO : locationDTOs) {
                    String locationName = locDTO.getLocationArea().getName();


                    Location location = findOrCreateLocation(em, locationName, locDTO);


                    pokemon.getLocations().add(location);
                    location.getPokemons().add(pokemon);
                }
            }

            em.getTransaction().commit();
            System.out.println("Database populated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Location findOrCreateLocation(EntityManager em, String name, LocationDTO dto) {
        try {
            return em.createQuery(
                            "SELECT l FROM Location l WHERE l.locationName = :name", Location.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            Location newLoc = new Location(dto);
            em.persist(newLoc);
            return newLoc;
        }
    }
}
