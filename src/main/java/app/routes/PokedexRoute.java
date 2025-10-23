package app.routes;

import app.controllers.PokedexController;
import app.entities.Pokedex;
import app.security.controllers.SecurityController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class  PokedexRoute {

    private final PokedexController controller = new PokedexController();
    SecurityController securityController = SecurityController.getInstance();

    protected EndpointGroup getRoutes() {

        return () -> {
            before("/*", securityController.authenticate());
            post("/{id}", controller::addPokemon);
            put("/{id}", controller::addPokemonToTeam);
        };
    }

}
