package app.entities;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.*;

@Embeddable
public class PokedexId implements Serializable {

    private String username;
    private int pokemonId;

    public PokedexId() {}

    public PokedexId(String username, int pokemonId) {
        this.username = username;
        this.pokemonId = pokemonId;
    }

    // getters/setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PokedexId)) return false;
        PokedexId that = (PokedexId) o;
        return username == that.username && pokemonId == that.pokemonId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, pokemonId);
    }
}
