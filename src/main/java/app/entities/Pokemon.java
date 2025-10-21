package app.entities;


import app.dtos.PokemonDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pokemon {

    @Id
    int id;

    String name;

    String type;


    // TODO: Ved ikke om den er rigtig endnu
    @ManyToOne
    Location location;

    public Pokemon (PokemonDTO dto){
        this.id = dto.getId();
        this.name = dto.getName();
        this.type = dto.getType();
    }
}
