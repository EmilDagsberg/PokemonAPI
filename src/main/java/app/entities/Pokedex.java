package app.entities;

import app.security.entities.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pokedex {

    @EmbeddedId
    private PokedexId id = new PokedexId();

    @ManyToOne
    @MapsId("username")  // Links to id.userId
    private User user;

    @ManyToOne
    @MapsId("pokemonId")  // Links to id.pokemonId
    private Pokemon pokemon;

    private boolean onTeam;
}
