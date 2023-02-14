package org.example.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    T save(T t);
    Integer delete(T t);
    Integer update(T t);
    List<T> findAll();
    Optional<T> findById(Integer id);
}
