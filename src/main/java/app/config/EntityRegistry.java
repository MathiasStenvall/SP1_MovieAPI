package app.config;

import app.entities.Actor;
import app.entities.Director;
import app.entities.Movie;
import org.hibernate.cfg.Configuration;

import java.awt.*;

final class EntityRegistry {

    private EntityRegistry() {}

    static void registerEntities(Configuration configuration) {
        configuration.addAnnotatedClass(Movie.class);
        configuration.addAnnotatedClass(Director.class);
        configuration.addAnnotatedClass(Actor.class);
    }
}