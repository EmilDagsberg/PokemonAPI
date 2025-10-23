package app.controllers;

import app.config.HibernateConfig;
import app.daos.PokedexDAO;
import app.daos.PokemonDAO;
import app.dtos.PokemonDTO;
import app.entities.Pokedex;
import dk.bugelhartmann.UserDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.Map;

public class PokedexController {
    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    private final PokedexDAO pokedexDAO = PokedexDAO.getInstance(emf);

    public void addPokemon(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        UserDTO user = ctx.attribute("user");

        if (user == null) {
            ctx.status(401).result("Unauthorized");
            return;
        }


        Pokedex pokedex = pokedexDAO.addPokemonToPokedex(user, id);

        ctx.status(201).json(Map.of(
                "msg", "Pokémon added to Pokédex",
                "user", user.getUsername(),
                "pokemon", pokedex.getPokemon().getName()
        ));
    }

}
