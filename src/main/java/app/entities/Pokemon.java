package app.entities;

import jakarta.persistence.*;
import app.dtos.PokemonDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

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

/*
    // TODO: Ved ikke om den er rigtig endnu
    @ManyToOne
            @JoinColumn(name = "location_id")
    Location location;
    */


//    @OneToMany(mappedBy = "pokemon", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Pokedex> pokedexEntries;

    public Pokemon(PokemonDTO pokemonDTO) {
        this.id = pokemonDTO.getId();
        this.name = pokemonDTO.getName();
        this.type = pokemonDTO.getFirstTypeName();
    }

}
