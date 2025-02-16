package ru.rental.service.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface Service<T, I> {

    Optional<T> get(I id);

    Optional<T> update(I id, T obj);

    T save(T obj);

    boolean delete(I id);

    List<T> filterBy(Predicate<T> predicate);

    List<T> getAll();
}
