package app.entities;

import app.dtos.LocationDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "location")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(unique = true)
    String locationName;


    @ManyToMany(mappedBy = "locations")
    private Set<Pokemon> pokemons = new HashSet<>();

    public Location(LocationDTO locationDTO) {
        this.locationName = locationDTO.getLocationArea().getName();
    }

}
