package app.controllers;

import app.config.HibernateConfig;
import app.daos.LocationDAO;
import app.entities.Location;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class LocationController {
    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    private final LocationDAO locationDAO = LocationDAO.getInstance(emf);

    public void getAllLocations(Context ctx){
        List<Location> locations = locationDAO.getAll();
        ctx.json(locations);
    }

    public void getLocationById(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        Location location = locationDAO.getById(id);
        if (location != null){
            ctx.json(location);
        } else {
            ctx.status(HttpStatus.NOT_FOUND).result("Location not found");
        }
    }

    public void createLocation(Context ctx){
        Location location =  ctx.bodyAsClass(Location.class);
        Location createdLocation = locationDAO.create(location);
        ctx.status(HttpStatus.CREATED).json(createdLocation);
    }

    public void updateLocation(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        Location location = ctx.bodyAsClass(Location.class);
        Location updatedLocation = locationDAO.getById(id);
        if (updatedLocation == null){
            ctx.status(HttpStatus.NOT_FOUND).result("Location not found");
            return;
        }
        updatedLocation.setLocationName(location.getLocationName());

        Location updated = locationDAO.update(updatedLocation);
        ctx.status(HttpStatus.CREATED).json(updated);
    }

    public void deleteLocation(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        boolean deleted = locationDAO.delete(id);
        if (deleted){
            ctx.json(true);
        } else  {
            ctx.status(HttpStatus.NOT_FOUND).result("Location not found");
        }
    }
}
