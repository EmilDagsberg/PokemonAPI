package app.config;

import app.dtos.LocationDTO;
import app.dtos.PokemonDTO;
import app.entities.Location;
import app.entities.Pokemon;
import app.services.LocationServices;
import app.services.PokemonServices;
import jakarta.persistence.EntityManagerFactory;

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

            List<LocationDTO> locationDTOS = locationServices.fetchPokemonLocations(pokemonDTOS);


            for (LocationDTO l: locationDTOS) {
                Location location = new Location(l);
                em.persist(location);

            }
            em.getTransaction().commit();
        }
    }
}
