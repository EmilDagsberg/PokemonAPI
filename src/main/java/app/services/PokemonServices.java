package app.services;

import app.dtos.PokemonDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class PokemonServices {

    public List<PokemonDTO> fetchFirstGenPokemon() {
        List<PokemonDTO> pokemonDTOList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        HttpClient httpClient = HttpClient.newHttpClient();

        int currentId = 1;

        try {
            do {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("https://pokeapi.co/api/v2/pokemon/" + currentId))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    String json = response.body();
                    PokemonDTO discoverResponse = objectMapper.readValue(json, PokemonDTO.class);

                    if (discoverResponse != null) {
                        pokemonDTOList.add(discoverResponse);
                    }
                    currentId++;
                } else {
                    System.out.println("Fejl ved læsning af Pokemon-API");
                    System.out.println("Fejl: " + response.body());
                    break;
                }
            } while (currentId <= 151);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pokemonDTOList;
    }
}
