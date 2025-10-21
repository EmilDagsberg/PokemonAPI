package app.dtos;

import app.entities.Pokemon;
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
    private int id;
    private String name;
    private List<TypeSlot> types;

    public PokemonDTO(Pokemon pokemon) {
        this.id = pokemon.getId();
        this.name = pokemon.getName();

        TypeInfo typeInfo = new TypeInfo();
        typeInfo.setName(pokemon.getType());

        TypeSlot typeSlot = new TypeSlot();
        typeSlot.setType(typeInfo);

        this.types = List.of(typeSlot);
    }

    // Optional helper: get the first type name directly
    public String getFirstTypeName() {
        return (types != null && !types.isEmpty())
                ? types.get(0).getType().getName()
                : null;
    }
}

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class TypeSlot {
    private TypeInfo type;
}

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class TypeInfo {
    private String name;
}

