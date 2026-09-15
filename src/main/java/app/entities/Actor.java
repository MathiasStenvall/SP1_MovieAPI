package app.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Actor {
    @Id
    private int id;

    private String name;

}
