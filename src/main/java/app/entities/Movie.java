package app.entities;

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
    @ManyToMany
    private List<Director> directors;
    @ManyToMany
    private List<Actor> actors;
    private List<String> genres;
    private double rating;
    private String title;
    private LocalDate releaseDate;
    private double popularity;


}
