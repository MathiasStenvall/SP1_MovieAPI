package app.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {

    private boolean adult;
    @JsonProperty("backdrop_path")
    private String backdropPath;
    @JsonProperty ("genre_ids")
    private List<Integer> genreIds;
    private int id;
    private String title;

                      "adult": false,
                      "backdrop_path": "/v67ZlaED8F7i4kupqj03cjcmNpZ.jpg",
                      "genre_ids": [99, 80],
                      "id": 1278804,
                      "title": "En farlig forbindelse: Mødtes på Snapchat",
                      "original_language": "da",
                      "original_title": "En farlig forbindelse: Mødtes på Snapchat",
                      "overview": "",
                      "popularity": 0.4,
                      "poster_path": "/kzAwuuQ5uTpRJMZZcF5UXqbNtZU.jpg",
                      "release_date": "2024-04-23",
                      "softcore": false,
                      "video": false,
                      "vote_average": 0,
                      "vote_count": 0




}
