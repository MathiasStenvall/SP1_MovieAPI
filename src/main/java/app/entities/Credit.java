package app.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Credit {
    @ManyToMany
    private Set<Crew> crew;
    @ManyToMany
    private Set<Actor> cast;

    public Set<Crew> getDirectors() {
        Set<Crew> directors = new HashSet<>();
        for (Crew c : this.crew) {
            if (c.getJob().equals("Director")) {
                directors.add(c);
            }
        }
        return directors;
    }


}
