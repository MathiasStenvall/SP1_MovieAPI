package app.DAO;

import app.config.HibernateTestConfig;
import app.entities.Actor;
import app.entities.Crew;
import app.entities.Genre;
import app.entities.Movie;
import app.testutils.MovieTestPopulator;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovieDAOTest {

    private final EntityManagerFactory emf = HibernateTestConfig.getEntityManagerFactory();
    private MovieDAO movieDAO;
    private Map<String, Movie> seeded;

    @BeforeEach
    void beforeEach() {
        movieDAO = new MovieDAO(emf);
        seeded = MovieTestPopulator.populate(emf);
    }

    @AfterAll
    void shutdown() {
        emf.close();
    }

    @Test
    void create() {
        Crew c1 = new Crew(3, "William", 2, "Science", "Crazy Scientist");

        Actor a1 = new Actor(3, "Thad", 2, "Reacher");

        Genre g1 = new Genre(3, "Documentary");

        Movie m1 = new Movie(3, List.of(c1), List.of(a1), List.of(g1), 8.1, "Revenge of the Scientist"
        , LocalDate.of(2026, 10, 31), 1700, 2000, 500.30);

        Movie savedMovie = movieDAO.create(m1);
        assertThat(m1, is(savedMovie));
    }

    @Test
    void get() {
        List<Movie> allMovies = movieDAO.get();

        assertThat(allMovies.size(), is(2));
        assertThat(allMovies.get(0).getTitle(), is(seeded.get("Movie1").getTitle()));
        assertThat(allMovies.get(1).getTitle(), is(seeded.get("Movie2").getTitle()));
    }

    @Test
    void getByID() {
        Movie expected = seeded.get("Movie1");
        Movie actual = movieDAO.getByID(expected.getId());
        assertThat(actual.getId(), is(expected.getId()));
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
        Movie toDelete = seeded.get("Movie1");
        boolean result = movieDAO.delete(toDelete);
        assertThat(result, is(true));
        assertThrows(EntityNotFoundException.class, () -> movieDAO.getByID(toDelete.getId()));
    }
}