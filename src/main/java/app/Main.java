package app;

import app.DAO.MovieDAO;
import app.config.HibernateConfig;
import app.entities.Genre;
import app.entities.Movie;
import app.service.MovieAPI;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        MovieAPI movieAPI = new MovieAPI();
        MovieDAO movieDAO = new MovieDAO(emf);
        List<Movie> movieList = movieDAO.get();

        /*
        List<Integer> Ids = movieAPI.allMoviesIds();
        System.out.println(Ids.size());

        List<Movie> movies = movieAPI.createMovies(Ids);
      
    }

}
