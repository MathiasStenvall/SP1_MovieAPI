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

        System.out.println(movieDAO.saveAllMovies(movies));
        */

        List<Genre> genres = movieDAO.getGenre();
        System.out.println("All genres:");
        for (Genre g: genres){
            System.out.println("\t" + g.getName());
        }

        String genre = "Drama";
        List<Movie> moviesByGenre = movieDAO.getMoviesByGenre(genre);
        System.out.println("Movies by genre: " + genre);
        for (Movie m : moviesByGenre) {
            System.out.println("\t" + m.getTitle());
        }

        String movieName = "Lego";
        List<Movie> moviesWithName = movieDAO.searchMovieByTitle(movieName);
        System.out.println("Movies with name: " + movieName);
        for (Movie m: moviesWithName){
            System.out.println("\t" + m.getTitle());
        }

        double averageRating = movieDAO.averageRatingAllMovies(movieList);
        System.out.println("Average of all movies: \n \t" + Math.round(averageRating*100.0)/100.0 + "/10");

        List<Movie> topTenRatedMovies = movieDAO.topTenRatedMovies(movieList);
        System.out.println("Top ten rated movies:");
        for (Movie m: topTenRatedMovies){
            System.out.println("\t" + m.getTitle() + ": " + m.getRating());
        }

        List<Movie> botTenRatedMovies = movieDAO.botTenRatedMovies(movieList);
        System.out.println("Bottom ten rated movies:");
        for (Movie m: botTenRatedMovies){
            System.out.println("\t" + m.getTitle() + ": " + m.getRating());
        }

        List<Movie> topTenPopularMovies = movieDAO.topTenPopularMovies(movieList);
        System.out.println("Top ten popular movies:");
        for (Movie m: topTenPopularMovies){
            System.out.println("\t" + m.getTitle() + ": " + m.getPopularity());
        }

    }

}
