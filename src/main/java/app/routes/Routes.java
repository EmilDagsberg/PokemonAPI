package app.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

    private final PokemonRoute pokemonRoute = new PokemonRoute();
    private final PokedexRoute pokedexRoute = new PokedexRoute();

    public EndpointGroup getRoutes() {
        return () -> {
            path("/pokemon", pokemonRoute.getRoutes());
            path("/pokedex", pokedexRoute.getRoutes());
        };
    }
}
