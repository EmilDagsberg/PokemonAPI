package app.routes;

import app.controllers.PokedexController;
import app.entities.Pokedex;
import app.security.controllers.SecurityController;
import app.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class  PokedexRoute {

    private final PokedexController controller = new PokedexController();
    SecurityController securityController = SecurityController.getInstance();

    protected EndpointGroup getRoutes() {

        return () -> {
            before("/*", securityController.authenticate());
            post("/{id}", controller::addPokemon,  Role.USER);
            put("/{id}", controller::addPokemonToTeam, Role.USER);
            get("/", controller::getPokedexByUser, Role.USER);
        };
    }

}
