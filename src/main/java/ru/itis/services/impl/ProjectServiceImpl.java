package ru.itis.services.impl;

import ru.itis.entities.Project;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.repositories.interfaces.ProjectRepository;
import ru.itis.services.interfaces.ProjectService;

import java.util.List;

public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
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
    public void update(Long id, Project updated) {
        Project existing = getById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        existing.setStatus(updated.getStatus());
        existing.setManagerId(updated.getManagerId());
        projectRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        projectRepository.deleteById(id);
    }
}

