package app.controllers;

import app.daos.PokedexDAO;
import app.daos.PokemonDAO;
import app.dtos.PokemonDTO;
import dk.bugelhartmann.UserDTO;
import io.javalin.http.Context;

import java.util.Map;

public class PokedexController {

    private final PokedexDAO pokedexDAO = new PokedexDAO();
    private final PokemonDAO pokemonDAO = new PokemonDAO();

    public void addPokemon(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        UserDTO user = ctx.attribute("user");

        if (user == null) {
            ctx.status(401).result("Unauthorized");
            return;
        }

        PokemonDTO pokemon = pokemonDAO.getById(id);
        pokedexDAO.addPokemonToPokedex(user, pokemon);

        ctx.status(201).json(Map.of(
                "msg", "Pokémon added to Pokédex",
                "user", user.getUsername(),
                "pokemon", pokemon.getName()
        ));
    }

}
