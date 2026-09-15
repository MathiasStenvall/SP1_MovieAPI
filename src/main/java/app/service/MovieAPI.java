package app.service;

public class MovieAPI {

    private final String urlToAllMovies = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=";
    private int urlPage = 1;
    private final String getUrlToAllMovies2 = "&release_date.gte=2021-09-15&sort_by=popularity.desc&with_origin_country=DK";

    private final String apiKey = System.getenv("API_KEY");



}
