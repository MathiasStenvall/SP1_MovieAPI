package app.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    private int id;
    @ManyToMany (cascade = CascadeType.PERSIST)
    private List<Crew> crew;
    @ManyToMany (cascade = CascadeType.PERSIST)
    private List<Actor> cast;
    @ManyToMany (cascade = CascadeType.PERSIST)
    private List<Genre> genres;
    @JsonProperty ("vote_average")
    private double rating;
    private String title;
    @JsonProperty ("release_date")
    private LocalDate releaseDate;
    private double popularity;
    private double budget;
    private double revenue;

}
