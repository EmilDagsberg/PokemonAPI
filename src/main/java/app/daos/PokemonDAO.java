package app.daos;

import app.dtos.PokemonDTO;
import app.entities.Pokemon;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class PokemonDAO implements IDAO <PokemonDTO, Integer> {

    private static EntityManagerFactory emf;

    @Override
    public PokemonDTO getById(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            Pokemon pokemon = em.find(Pokemon.class, integer);
            return new PokemonDTO(pokemon);
        }
    }

    @Override
    public List<PokemonDTO> getAll() {
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<PokemonDTO> query = em.createQuery("SELECT new app.dtos.PokemonDTO(p) FROM Pokemon p", PokemonDTO.class);
            return query.getResultList();
        }
    }

    @Override
    public PokemonDTO create(PokemonDTO dto) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Pokemon pokemon = new Pokemon(dto);
            em.persist(dto);
            em.getTransaction().commit();
            em.close();
            return new PokemonDTO(pokemon);
        }
    }

    @Override
    public PokemonDTO update(Integer integer, PokemonDTO dto) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Pokemon pokemon = new Pokemon(dto);
            em.merge(pokemon);
            em.getTransaction().commit();
            em.close();
            return new PokemonDTO(pokemon);
        }
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
