package app.controllers;

import app.config.HibernateConfig;
import app.daos.PokemonDAO;
import app.dtos.PokemonDTO;
import app.entities.Pokemon;
import app.mappers.PokemonMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class PokemonController {
    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    private final PokemonDAO pokemonDAO = PokemonDAO.getInstance(emf);

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
        ctx.res().setStatus(201);

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
        ctx.res().setStatus(200);

    }

    public void deletePokemon(Context ctx){
        int id =  Integer.parseInt(ctx.pathParam("id"));
        boolean pokemonDTO = pokemonDAO.delete(id);
        if (pokemonDTO) {
            ctx.json(true);
            ctx.res().setStatus(204);
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
}
