package app.entities;

import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Credit {
    @ManyToMany
    private List<Crew> crew;
    @ManyToMany
    private List<Actor> cast;

    public List<Crew> getDirectors(){
        List<Crew> directors = new ArrayList<>();
        for (Crew c: this.crew){
            if (c.getJob().equals("Director")){
                directors.add(c);
            }
        }
        return directors;
    }



}
