package app.DAO;

import java.util.Set;

public interface IDAO<T> {
    T create(T t);

    Set<T> get();

    T getByID(int id);

    T update(T t);

    boolean delete(T t);
}