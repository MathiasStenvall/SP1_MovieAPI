package app.DAO;

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

    /*
    public boolean saveAllMovies(List<Movie> movieList) {
        try (EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            for (int i = 0; i < movieList.size(); i++) {
                em.persist(movieList.get(i));

                if (i % 50 == 0){
                    em.flush();
                    em.clear();
                }
            }
        }
        return true;
    }
    */


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

/*
public boolean saveAllMovies(List<Movie> movieList) {
    try (EntityManager em = emf.createEntityManager()) {
        em.getTransaction().begin();

        // 1. Saml alle unikke relaterede entities på tværs af HELE listen
        Map<Integer, Genre> genreMap = new HashMap<>();
        Map<Integer, Actor> actorMap = new HashMap<>();
        Map<Integer, Crew> crewMap = new HashMap<>();

        for (Movie movie : movieList) {
            for (Genre g : movie.getGenres()) genreMap.putIfAbsent(g.getId(), g);
            for (Actor a : movie.getCast()) actorMap.putIfAbsent(a.getId(), a);
            for (Crew c : movie.getCrew()) crewMap.putIfAbsent(c.getId(), c);
        }

        // 2. Persistér de unikke entities FØRST, alene
        genreMap.values().forEach(em::persist);
        actorMap.values().forEach(em::persist);
        crewMap.values().forEach(em::persist);
        em.flush();

        // 3. Erstat referencerne i hver Movie med de "kanoniske" (nu managed) instanser
        for (Movie movie : movieList) {
            movie.setGenres(movie.getGenres().stream()
                    .map(g -> genreMap.get(g.getId())).toList());
            movie.setCast(movie.getCast().stream()
                    .map(a -> actorMap.get(a.getId())).toList());
            movie.setCrew(movie.getCrew().stream()
                    .map(c -> crewMap.get(c.getId())).toList());
        }

        // 4. Nu kan du roligt persistere filmene i batches med flush/clear,
        //    fordi de relaterede entities allerede er fuldt gemt og flushet
        for (int i = 0; i < movieList.size(); i++) {
            em.persist(movieList.get(i));
            if (i % 50 == 0) {
                em.flush();
                em.clear();
            }
        }

        em.getTransaction().commit();
        return true;
    }
}


 */


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

    //TODO AOI:


    public List<Genre> getGenre() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Genre> query = em.createQuery("SELECT g FROM Genre g", Genre.class);
            return query.getResultList();
        }
    }

    public List<Movie> getMoviesByGenre(String genreName){
        List<Movie> movieList = get();

        return movieList.stream().filter(movie -> movie.getGenres()
                .stream().anyMatch(genre -> genre.getName().equalsIgnoreCase(genreName))).toList();


        /*
        try (EntityManager em = emf.createEntityManager()){
            TypedQuery<Movie> query = em.createQuery("SELECT DISTINCT m FROM Movie m JOIN m.genres g LEFT JOIN FETCH m.crew, m.cast WHERE g.name = :genreName", Movie.class);
            query.setParameter("genreName", genreName);
            return query.getResultList();
        }
        */
    }

    public List<Movie> searchMovieByTitle (String movieName){
        List<Movie> movieList = get();
        return movieList.stream().filter(movie -> movie.getTitle().toLowerCase().contains(movieName.toLowerCase())).toList();
    }

    public double averageRatingAllMovies (List<Movie> movieList){
        double total = 0;
        for (Movie m: movieList){
            total += m.getRating();
        }
        return total/movieList.size();
    }

    public List<Movie> topTenRatedMovies(List<Movie> movieList){
        List<Movie> topMovies = sortByRatingAsc(movieList);
        int index = Math.max(0, topMovies.size()-10);
        return new ArrayList<>(topMovies.subList(index, topMovies.size()));
    }

    public List<Movie> botTenRatedMovies(List<Movie> movieList){
        List<Movie> botMovies = sortByRatingAsc(movieList);
        int index = Math.min(10, botMovies.size());
        return new ArrayList<>(botMovies.subList(0, index));
    }

    private List<Movie> sortByRatingAsc(List<Movie> movieList){
        return movieList.stream().sorted(Comparator.comparing(Movie::getRating)).toList();
    }

    public List<Movie> topTenPopularMovies (List<Movie> movieList){
        List<Movie> topMovies = movieList.stream().sorted(Comparator.comparing(Movie::getPopularity).reversed()).toList();
        int index = Math.min(10, topMovies.size());
        return new ArrayList<>(topMovies.subList(0, index));
    }
}