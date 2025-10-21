package app.daos;

import app.entities.Pokemon;
import app.security.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class UserDAO implements IDAO <User, Integer> {

    private static EntityManagerFactory emf;

    @Override
    public User getById(Integer integer) {
        try(EntityManager em = emf.createEntityManager()){
            return em.find(User.class, integer);
        }
    }

    @Override
    public List<User> getAll() {
        try(EntityManager em = emf.createEntityManager()){
            return em.createQuery("SELECT u FROM User u", User.class)
                    .getResultList();
        }
    }

    @Override
    public User create(User user) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            em.close();
        }
        //Skal måske returnere DTO i stedet?
        return user;
    }

    @Override
    public User update(Integer integer, User user) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
            em.close();
        }
        //Skal måske returnere DTO i stedet?
        return user;
    }

    @Override
    public boolean delete(Integer integer) {
        EntityManager em = emf.createEntityManager();

        try{
            Pokemon hotel = em.find(Pokemon.class, integer);

            em.getTransaction().begin();
            em.remove(hotel);
            em.getTransaction().commit();
//            logger.info("Pokemon with ID: " + hotel.getId() + " has been deleted");
            // TODO: Tilføj fejlbesked. Skal Logger bruges?
            return true;
        } catch (Exception e) {
            if(em.getTransaction() != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
//            logger.info("Something went wrong. Changes have been rolled back");
            // TODO: Tilføj fejlbesked. Skal Logger bruges?
            e.printStackTrace();
            return false;
        }
    }
}
