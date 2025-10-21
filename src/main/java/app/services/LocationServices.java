package app.services;

import app.dtos.LocationDTO;
import app.dtos.LocationResponseDTO;
import app.dtos.PokemonDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class LocationServices {
    public List<LocationDTO> fetchPokemonLocations(List<PokemonDTO> pokemonList) {
        List<LocationDTO> locationDTOList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        HttpClient httpClient = HttpClient.newHttpClient();

        try {
            for (PokemonDTO pokemonDTO : pokemonList) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("http://pokeapi.co/api/v2/pokemon/" + pokemonDTO.getId() + "/encounters"))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    String json = response.body();
                    LocationResponseDTO discoverResponse = objectMapper.readValue(json, LocationResponseDTO.class);

                    if (discoverResponse.getResults() != null && !discoverResponse.getResults().isEmpty()) {
                        locationDTOList.addAll(discoverResponse.getResults());
                    }
                } else {
                    System.out.println("Fejl ved læsning af Pokemon-API");
                    System.out.println("Fejl: " + response.body());
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return locationDTOList;
    }
}
