package app.mappers;

import app.dtos.PokemonDTO;
import app.entities.Pokemon;

public class PokemonMapper {

    public static PokemonDTO toDTO(Pokemon pokemon){
        return new PokemonDTO(pokemon);
    }

    public static Pokemon toEntity(PokemonDTO pokemonDTO){
        return new Pokemon(pokemonDTO);
    }
}
