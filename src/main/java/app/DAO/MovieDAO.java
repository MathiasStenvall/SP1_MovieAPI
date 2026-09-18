package app.DAO;

import app.entities.Actor;
import app.entities.Crew;
import app.entities.Genre;
import app.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;

import java.util.*;
import java.util.stream.Collectors;

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

    public boolean saveAllMovies(List<Movie> movieList) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Map<Integer, Actor> actorCache = new HashMap<>();
            Map<Integer, Crew> crewCache = new HashMap<>();
            Map<Integer, Genre> genreCache = new HashMap<>();

            for (int i = 0; i < movieList.size(); i++) {
                Movie movie = movieList.get(i);

                movie.setCast(movie.getCast().stream()
                        .map(a -> actorCache.computeIfAbsent(a.getId(), id -> a))
                        .collect(Collectors.toSet()));
                movie.setCrew(movie.getCrew().stream()
                        .map(c -> crewCache.computeIfAbsent(c.getId(), id -> c))
                        .collect(Collectors.toSet()));
                movie.setGenres(movie.getGenres().stream()
                        .map(g -> genreCache.computeIfAbsent(g.getId(), id -> g))
                        .collect(Collectors.toSet()));

                em.persist(movie);

            }

            em.getTransaction().commit();
            return true;
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @Override
    public List<Movie> get() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Movie> movies = em.createQuery("SELECT DISTINCT m FROM Movie m " +
                            "LEFT JOIN FETCH m.genres " +
                            "LEFT JOIN FETCH m.crew " +
                            "LEFT JOIN FETCH m.cast",
                    Movie.class);
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

    public List<Genre> getGenre() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Genre> query = em.createQuery("SELECT g FROM Genre g", Genre.class);
            return query.getResultList();
        }
    }

    public List<Movie> getMoviesByGenre(String genreName) {
        List<Movie> movieList = get();

        return movieList.stream().filter(movie -> movie.getGenres()
                .stream().anyMatch(genre -> genre.getName().equalsIgnoreCase(genreName))).toList();
    }

    public List<Movie> searchMovieByTitle(String movieName) {
        List<Movie> movieList = get();
        return movieList.stream().filter(movie -> movie.getTitle().toLowerCase().contains(movieName.toLowerCase())).toList();
    }

    public double averageRatingAllMovies(List<Movie> movieList) {
        double total = 0;
        for (Movie m : movieList) {
            total += m.getRating();
        }
        return total / movieList.size();
    }

    public List<Movie> topTenRatedMovies(List<Movie> movieList) {
        List<Movie> topMovies = sortByRatingAsc(movieList);
        int index = Math.max(0, topMovies.size() - 10);
        return new ArrayList<>(topMovies.subList(index, topMovies.size()));
    }

    public List<Movie> botTenRatedMovies(List<Movie> movieList) {
        List<Movie> botMovies = sortByRatingAsc(movieList);
        int index = Math.min(10, botMovies.size());
        return new ArrayList<>(botMovies.subList(0, index));
    }

    private List<Movie> sortByRatingAsc(List<Movie> movieList) {
        return movieList.stream().sorted(Comparator.comparing(Movie::getRating)).toList();
    }

    public List<Movie> topTenPopularMovies(List<Movie> movieList) {
        List<Movie> topMovies = movieList.stream().sorted(Comparator.comparing(Movie::getPopularity).reversed()).toList();
        int index = Math.min(10, topMovies.size());
        return new ArrayList<>(topMovies.subList(0, index));
    }
}