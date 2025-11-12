package ru.itis.services.impl;

import ru.itis.entities.Project;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.exceptions.ProjectNotEmptyException;
import ru.itis.repositories.interfaces.ProjectRepository;
import ru.itis.repositories.interfaces.SprintRepository;
import ru.itis.repositories.interfaces.TaskRepository;
import ru.itis.services.interfaces.ProjectService;

import java.util.List;

public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final SprintRepository sprintRepository;
    private final TaskRepository taskRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, SprintRepository sprintRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.sprintRepository = sprintRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public void create(Project project) {
        projectRepository.save(project);
    }

    @Override
    public Project getById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
    }

    @Override
    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    @Override
    public void update(Long projectId, Project updated) {
        Project existing = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Проект не найден"));
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        existing.setStatus(updated.getStatus());
        existing.setManagerId(updated.getManagerId());
        projectRepository.update(projectId, existing);
    }

    @Override
    public void delete(Long projectId) {
        if (sprintRepository.countByProjectId(projectId) > 0 ||
                taskRepository.countByProjectId(projectId) > 0) {
            throw new ProjectNotEmptyException("Нельзя удалить проект: к нему привязаны спринты или задачи");
        }
        projectRepository.deleteById(projectId);
    }

}

