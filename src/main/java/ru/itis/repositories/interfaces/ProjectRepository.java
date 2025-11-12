package ru.itis.repositories.interfaces;

import ru.itis.entities.Project;
import ru.itis.util.CrudRepository;

import java.util.List;

public interface ProjectRepository extends CrudRepository<Project, Long> {
    List<Project> findByManagerId(Long managerId);
    List<Project> findByStatus(String status);
}
