package app.config;

import jakarta.persistence.EntityManagerFactory;

public class Populate {
    public static void main(String[] args) {
        EntityManagerFactory emf= HibernateConfig.getEntityManagerFactory();

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
        }
    }
}
