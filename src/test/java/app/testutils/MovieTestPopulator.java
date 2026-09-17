package app.testutils;

import app.entities.Actor;
import app.entities.Crew;
import app.entities.Genre;
import app.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import lombok.ToString;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MovieTestPopulator {

    public static Map<String, Movie> populate(EntityManagerFactory emf) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            LocalDate testDate = LocalDate.now();

            Crew c1 = new Crew(1, "Nicoline", 1, "Photography", "backgrounds");
            Crew c2 = new Crew(2, "Mathias", 2, "Stuntman", "Stunt double");

            Actor a1 = new Actor(1, "Jon", 2, "Terminator");
            Actor a2 = new Actor(2, "Sarah", 1, "Lilith");

            Genre g1 = new Genre(1, "Adventure");
            Genre g2 = new Genre(2, "Romance");

            Movie m1 = new Movie(1, List.of(c1), List.of(a1), List.of(g1), 6.4, "Ghost of Japan", testDate
                    , 12000, 35000, 170000);
            Movie m2 = new Movie(2, List.of(c2), List.of(a2), List.of(g2), 9.8, "Met last summer", testDate.plusDays(5)
                    , 27000, 50000, 320000);

            try {
                em.createNativeQuery("TRUNCATE TABLE movie, crew, actor, genre RESTART IDENTITY CASCADE").executeUpdate();
                em.persist(m1);
                em.persist(m2);
                em.flush();
            } catch (PersistenceException e) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                throw e;
            }
            em.getTransaction().commit();

            Map<String, Movie> seeded = new LinkedHashMap<>();
            seeded.put("Movie1", m1);
            seeded.put("Movie2", m2);
            return seeded;
        }
    }

}
