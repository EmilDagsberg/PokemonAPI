package app.controllers;

import app.daos.PokemonDAO;
import app.dtos.PokemonDTO;
import app.entities.Pokemon;
import app.mappers.PokemonMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PokemonController {

    private final PokemonDAO pokemonDAO = new PokemonDAO();

    public void getAllPokemons(Context ctx){
        List<PokemonDTO> pokemonDTOs = pokemonDAO.getAll();
        ctx.json(pokemonDTOs);
    }

    public void getPokemonById(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        PokemonDTO pokemonDTO = pokemonDAO.getById(id);

        if (pokemonDTO != null) {
            ctx.json(pokemonDTO);
        } else {
            ctx.status(HttpStatus.NOT_FOUND).result("Pokemon Not Found");
        }

    }


    public void createPokemon(Context ctx){
        PokemonDTO pokemonDTO = ctx.bodyAsClass(PokemonDTO.class);
        PokemonDTO pokemon = pokemonDAO.create(pokemonDTO);
        ctx.status(HttpStatus.CREATED).json(pokemon);
    }

    public void updatePokemon(Context ctx){
        int id =  Integer.parseInt(ctx.pathParam("id"));
        PokemonDTO pokemonDTO = ctx.bodyAsClass(PokemonDTO.class);
        PokemonDTO pokemon = pokemonDAO.getById(id);
        if (pokemon == null) {
            ctx.status(HttpStatus.NOT_FOUND).result("Pokemon Not Found");
        }
        pokemon.setName(pokemonDTO.getName());
        pokemon.setTypes(pokemonDTO.getTypes());
        PokemonDTO updated = pokemonDAO.update(pokemon);
        ctx.status(HttpStatus.CREATED).json(updated);
    }

    public void deletePokemon(Context ctx){
        int id =  Integer.parseInt(ctx.pathParam("id"));
        boolean pokemonDTO = pokemonDAO.delete(id);
        if (pokemonDTO) {
            ctx.json(true);
        }
        else {
            ctx.status(HttpStatus.NOT_FOUND).result("Pokemon Not Found");
        }
    }

    public void populate(Context ctx){
        pokemonDAO.populate();
        ctx.res().setStatus(200);
        ctx.json("{ \"message\": \"Database has been populated\" }");
    }


    public void getRandomPokemonByType(Context ctx){
        String type = ctx.pathParam("type");
        if (type == null || type.isBlank()) {
            ctx.status(400).json("Invalid type");
            return;
        }
        PokemonDAO pokemonDAO = new PokemonDAO();
        List<PokemonDTO> matches = pokemonDAO.getPokemonByType(type);

        if (matches == null) {
            ctx.status(400).json("No matches found for this type");
            return;
        }

        Collections.shuffle(matches);
        PokemonDTO randomPokemon = matches.get(0);

        ctx.status(200).json(randomPokemon);
    }

    public void getPokemonsByType(Context ctx){
        String type = ctx.pathParam("type");
        if (type == null || type.isBlank()) {
            ctx.status(400).json("Invalid type");
            return;
        }
        PokemonDAO pokemonDAO = new PokemonDAO();
        List<PokemonDTO> matches = pokemonDAO.getPokemonByType(type);

        if (matches == null) {
            ctx.status(400).json("No matches found for this type");
            return;
        }

        ctx.json(matches).status(200);

    }
}
