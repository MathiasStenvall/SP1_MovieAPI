package app;

import app.service.MovieAPI;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        MovieAPI movieAPI = new MovieAPI();
        List<Integer> Ids = movieAPI.allMoviesIds();
        System.out.println(Ids);
        System.out.println(Ids.size());


    }

}
