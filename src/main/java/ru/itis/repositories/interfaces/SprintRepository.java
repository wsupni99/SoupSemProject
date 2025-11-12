package ru.itis.repositories.interfaces;

import ru.itis.entities.Sprint;
import ru.itis.util.CrudRepository;

import java.util.List;

public interface SprintRepository extends CrudRepository<Sprint, Long> {
    List<Sprint> findByProjectId(Long projectId);
    int countByProjectId(long projectId);
}