package app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TypeSlot {
    private TypeInfo type;
}

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class TypeInfo {
    private String name;
}
