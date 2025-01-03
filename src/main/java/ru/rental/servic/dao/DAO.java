package ru.rental.servic.dao;

import java.util.List;
import java.util.function.Predicate;

public interface DAO<T, I> {

    T get(I id);

    T update(I id, T obj);

    T save(T obj);

    boolean delete(I id);

    List<T> filterBy(Predicate<T> predicate);
}
