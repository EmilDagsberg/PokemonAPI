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
public class PokemonDTO {

    private static final String SPRITE_BASE_URL =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/";

    private int id;
    private String name;
    private List<TypeSlot> types;
    private List<String> locations;
    private String sprite;

    public PokemonDTO(Pokemon pokemon) {
        this.id = pokemon.getId();
        this.name = pokemon.getName();

        TypeInfo typeInfo = new TypeInfo();
        typeInfo.setName(pokemon.getType());

        TypeSlot typeSlot = new TypeSlot();
        typeSlot.setType(typeInfo);

        this.types = List.of(typeSlot);

        this.locations = pokemon.getLocations()
                .stream()
                .map(Location::getLocationName)
                .toList();

        // Front default sprite (derived from ID)
        this.sprite = SPRITE_BASE_URL + id + ".png";
    }

    @JsonIgnore
    public String getFirstTypeName() {
        return (types != null && !types.isEmpty())
                ? types.get(0).getType().getName()
                : null;
    }
}
