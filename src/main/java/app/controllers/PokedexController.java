package app.controllers;

import app.daos.PokedexDAO;
import app.daos.PokemonDAO;
import app.dtos.PokemonDTO;
import app.entities.Pokedex;
import dk.bugelhartmann.UserDTO;
import io.javalin.http.Context;

import java.util.Map;

public class PokedexController {

    private final PokedexDAO pokedexDAO = new PokedexDAO();

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
