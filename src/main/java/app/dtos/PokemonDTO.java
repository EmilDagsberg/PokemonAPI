package app.dtos;

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
