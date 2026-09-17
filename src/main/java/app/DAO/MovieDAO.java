package app.DAO;

import app.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class MovieDAO implements IDAO<Movie> {

    EntityManagerFactory emf;

    public MovieDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Movie create(Movie movie) {
        try (EntityManager em = emf.createEntityManager()) {
            Movie foundMovie = em.find(Movie.class, movie.getId());
            if (foundMovie != null) throw new IllegalArgumentException("Movie is already saved in our database");
            em.getTransaction().begin();
            em.persist(movie);
            em.getTransaction().commit();
            return movie;
        }
    }

    @Override
    public List<Movie> get() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Movie> movies = em.createQuery("SELECT m FROM Movie m", Movie.class);
            return movies.getResultList();
        }
    }

    @Override
    public Movie getByID(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            Movie movie = em.find(Movie.class, id);
            if (movie == null) throw new EntityNotFoundException("No Movie found with id nr: " + id);
            return movie;
        }
    }

    @Override
    public Movie update(Movie movie) {
        try (EntityManager em = emf.createEntityManager()) {
            Movie foundMovie = em.find(Movie.class, movie.getId());
            if (foundMovie == null) throw new EntityNotFoundException("No Movie found with id nr: " + movie.getId());
            em.getTransaction().begin();
            Movie updatedMovie = em.merge(movie);
            em.getTransaction().commit();
            return updatedMovie;
        }
    }

    @Override
    public boolean delete(Movie movie) {
        try (EntityManager em = emf.createEntityManager()) {
            Movie foundMovie = em.find(Movie.class, movie.getId());
            if (foundMovie == null) throw new EntityNotFoundException("Movie not found!");
            em.getTransaction().begin();
            em.remove(movie);
            em.getTransaction().commit();
            return true;
        }
    }
}
