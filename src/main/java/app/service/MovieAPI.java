package app.service;

import app.DTO.APIResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieAPI {

    private final String urlToAllMovies = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=";
    private int urlPage = 1;
    private final String urlToAllMovies2 = "&release_date.gte=2021-09-15&sort_by=popularity.desc&with_origin_country=DK&api_key=";

    private final String apiKey = System.getenv("API_KEY");

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Integer> allMoviesIds() {
        List<Integer> movieIds = new ArrayList<>();
        try {
            APIResponse apiResponse = objectMapper.readValue(new URL((urlToAllMovies + urlPage + urlToAllMovies2 + apiKey)),
                    APIResponse.class);
            int page = apiResponse.getPage();
            int totalPages = apiResponse.getTotalPages();

            while (totalPages >= page){
                APIResponse response = objectMapper.readValue(new URL((urlToAllMovies + page + urlToAllMovies2 + apiKey)),
                        APIResponse.class);
                for (APIResponse.Result r: response.getResults()){
                    movieIds.add(r.getId());
                }
                page++;
            }

            return movieIds;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
