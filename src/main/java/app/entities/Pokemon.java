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

import java.util.HashSet;
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


    /*@ManyToMany
    @JoinTable(
            name = "pokemon_location",
            joinColumns = @JoinColumn(name = "pokemon_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    private Set<Location> locations = new HashSet<>();
*/


//    @OneToMany(mappedBy = "pokemon", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Pokedex> pokedexEntries;

    public Pokemon(PokemonDTO pokemonDTO) {
        this.id = pokemonDTO.getId();
        this.name = pokemonDTO.getName();
        this.type = pokemonDTO.getFirstTypeName();
    }

//    public void setLocations(Set<Location> locations) {
//        this.locations.clear();
//        if (locations != null) {
//            for (Location location : locations) {
//                addLocation(location);
//            }
//        }
//    }
//
//
//    public void addLocation(Location location) {
//        if (location != null) {
//            this.locations.add(location);
//            location.getPokemons().add(this);
//        }
//    }


}
