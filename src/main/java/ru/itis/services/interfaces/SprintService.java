package ru.itis.services.interfaces;

import ru.itis.entities.Sprint;

import java.util.List;

public interface SprintService {
    void create(Sprint sprint);
    Sprint getById(Long id);
    List<Sprint> getAll();
    void update(Sprint updated);
    void delete(Long id);
    List<Sprint> getAllSprints();
    List<Sprint> getByProjectId(Long projectId);
    String getProjectNameBySprintId(Long sprintId);
}