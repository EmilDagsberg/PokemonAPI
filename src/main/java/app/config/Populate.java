package app.config;

import app.dtos.PokemonDTO;
import app.entities.Pokemon;
import app.services.PokemonServices;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class Populate {
    public static void main(String[] args) {
        EntityManagerFactory emf= HibernateConfig.getEntityManagerFactory();
        PokemonServices pokemonServices = new PokemonServices();

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            List<PokemonDTO> pokemonDTOS = pokemonServices.fetchFirstGenPokemon();
            for (PokemonDTO p: pokemonDTOS) {
                Pokemon pokemon = new Pokemon(p);
                em.persist(pokemon);
            }
            em.getTransaction().commit();
        }
    }
}
