package app.dtos;

import app.entities.Location;
import app.entities.Pokemon;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PokedexEntryDTO {

    private static final String SPRITE_BASE_URL =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/";

    private int id;
    private String name;
    private List<TypeSlot> types;
    private List<String> locations;
    private String sprite;
    private boolean onTeam;

    public PokedexEntryDTO (PokemonDTO pokemon) {
        this.id = pokemon.getId();
        this.name = pokemon.getName();

        this.types = pokemon.getTypes();

        this.locations = pokemon.getLocations();

        // Front default sprite (derived from ID)
        this.sprite = SPRITE_BASE_URL + id + ".png";
    }
}
