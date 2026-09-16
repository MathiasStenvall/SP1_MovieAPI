package app.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@JsonIgnoreProperties (ignoreUnknown = true)
public class Crew {
    @Id
    private int id;
    private String name;
    private String gender;
    private String department;
    private String job;

    public Crew(int id, String name, int gender, String department, String job) {
        this.id = id;
        this.name = name;
        determineGender(gender);
        this.department = department;
        this.job = job;
    }

    private void determineGender(int input){
        if (input == 1){
            this.gender = "Female";
        } else {
            this.gender = "Male";
        }
    }

}
