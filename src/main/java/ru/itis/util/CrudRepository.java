package ru.itis.util;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    List<T> findAll();
    Optional<T> findById(ID id);
    void save(T entity);
    void update(Long projectId, T entity);
    void deleteById(ID id);
}