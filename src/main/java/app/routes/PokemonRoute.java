package app.routes;

import app.controllers.PokemonController;
import app.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class PokemonRoute {

    private final PokemonController controller = new PokemonController();

    protected EndpointGroup getRoutes() {

        return () -> {
            get("/populate", controller::populate, Role.ADMIN);
            post("/", controller::createPokemon, Role.ADMIN);
            get("/", controller::getAllPokemons);

            //NON CRUD
            get("/random/{type}", controller::getRandomPokemonByType);
            get("/type/{type}", controller::getPokemonsByType);
            get("/random", controller::getRandomPokemon);
            // NON CRUD

            get("/{id}", controller::getPokemonById);
            put("/{id}", controller::updatePokemon, Role.ADMIN);
            delete("/{id}", controller::deletePokemon, Role.ADMIN);
        };
    }
}
