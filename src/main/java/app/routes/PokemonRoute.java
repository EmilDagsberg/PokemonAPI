package app.routes;

import app.controllers.PokemonController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class PokemonRoute {

    private final PokemonController controller = new PokemonController();

    protected EndpointGroup getRoutes() {

        return () -> {
            get("/populate", controller::populate);
            post("/", controller::createPokemon);
            get("/", controller::getAllPokemons);
            get("/{id}", controller::getPokemonById);
            put("/{id}", controller::updatePokemon);
            delete("/{id}", controller::deletePokemon);
        };
    }
}
