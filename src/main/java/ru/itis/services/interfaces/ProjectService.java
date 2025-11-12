package ru.itis.services.interfaces;

import ru.itis.entities.Project;

import java.util.List;

public interface ProjectService {
    void create(Project project);
    Project getById(Long id);
    List<Project> getAll();
    void update(Long id, Project updated);
    void delete(Long id);
}
