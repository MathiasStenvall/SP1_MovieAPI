package app.DTO;

import app.entities.Credit;
import app.entities.Genre;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties (ignoreUnknown = true)
public class MovieDTO {

    private double budget;
    private Set<Genre> genres;
    @Id
    private int id;
    @JsonProperty ("original_title")
    private String originalTitle;
    private String overview;
    @JsonProperty ("release_date")
    private LocalDate releaseDate;
    private double popularity;
    private double revenue;
    @JsonProperty ("vote_average")
    private double rating;
    private Credit credits;

}
