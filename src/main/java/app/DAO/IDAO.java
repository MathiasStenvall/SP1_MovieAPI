package app.DAO;

import java.util.List;

public interface IDAO<T> {
    T create(T t);

    List<T> get();

    T getByID(int id);

    T update(T t);

    boolean delete(T t);
}