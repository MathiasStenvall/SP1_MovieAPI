package app;

import app.config.HibernateConfig;
import app.entities.Movie;
import app.service.MovieAPI;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        /*
        MovieAPI movieAPI = new MovieAPI();
        List<Integer> Ids = movieAPI.allMoviesIds();
        System.out.println(Ids.size());

        List<Movie> movies = movieAPI.createMovies(Ids);
      
    }

}
