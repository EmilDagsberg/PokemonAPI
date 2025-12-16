package app.entities;

import app.dtos.PokemonDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pokemon {

    private static final String SPRITE_BASE_URL =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/";

    @Id
    private int id;

    private String name;

    private String type;

    @Column(nullable = false)
    private String sprite;

    @ManyToMany
    @JoinTable(
            name = "pokemon_location",
            joinColumns = @JoinColumn(name = "pokemon_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    private Set<Location> locations = new HashSet<>();

    public Pokemon(PokemonDTO pokemonDTO) {
        this.id = pokemonDTO.getId();
        this.name = pokemonDTO.getName();
        this.type = pokemonDTO.getFirstTypeName();
        this.sprite = pokemonDTO.getSprite();
    }

    // 🔑 Guarantee sprite before insert
    @PrePersist
    public void ensureSprite() {
        if (this.sprite == null || this.sprite.isBlank()) {
            this.sprite = SPRITE_BASE_URL + this.id + ".png";
        }
    }
}
