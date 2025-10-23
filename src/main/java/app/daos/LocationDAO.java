package app.daos;

import app.config.HibernateConfig;
import app.entities.Location;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class LocationDAO implements IDAO<Location, Integer>{

    private static LocationDAO instance;
    private static EntityManagerFactory emf;

    private LocationDAO() {}

    public static LocationDAO getInstance(EntityManagerFactory emf) {
        if(instance == null) {
            instance = new LocationDAO();
            LocationDAO.emf = emf;
        }
        return instance;
    }

    @Override
    public Location getById(Integer integer) {
        try(EntityManager em = emf.createEntityManager()){
            return em.find(Location.class, integer);
        }
    }

    @Override
    public List<Location> getAll() {
        try(EntityManager em = emf.createEntityManager()){
            return em.createQuery("SELECT l FROM Location l", Location.class)
                    .getResultList();
        }
    }

    @Override
    public Location create(Location location) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(location);
            em.getTransaction().commit();
            em.close();
            return location;
        }
    }

    @Override
    public Location update(Location location) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Location merged = em.merge(location);
            em.getTransaction().commit();
            em.close();
            return merged;
        }
    }

    @Override
    public boolean delete(Integer integer) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Location location = em.find(Location.class, integer);
            if (location == null){
                return false;
            }
            em.remove(location);
            em.getTransaction().commit();
            em.close();
            return true;
        }
    }
}
