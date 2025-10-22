package app.config;

import app.dtos.LocationDTO;
import app.dtos.PokemonDTO;
import app.entities.Location;
import app.entities.Pokemon;
import app.services.LocationServices;
import app.services.PokemonServices;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Populate {
    public static void main(String[] args) {
        EntityManagerFactory emf= HibernateConfig.getEntityManagerFactory();
        PokemonServices pokemonServices = new PokemonServices();
        LocationServices locationServices = new LocationServices();

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            List<PokemonDTO> pokemonDTOS = pokemonServices.fetchFirstGenPokemon();
            Map<Integer, Pokemon> pokemonMap = new HashMap<>();


            for (PokemonDTO p: pokemonDTOS) {
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
