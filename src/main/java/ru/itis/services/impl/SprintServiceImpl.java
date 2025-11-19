package ru.itis.services.impl;

import ru.itis.entities.Project;
import ru.itis.entities.Sprint;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.repositories.interfaces.SprintRepository;
import ru.itis.services.interfaces.ProjectService;
import ru.itis.services.interfaces.SprintService;

import java.util.List;

public class SprintServiceImpl implements SprintService {
    private final SprintRepository sprintRepository;
    private final ProjectService projectService;

    public SprintServiceImpl(SprintRepository sprintRepository, ProjectService projectService) {
        this.sprintRepository = sprintRepository;
        this.projectService = projectService;
    }

    @Override
    public void create(Sprint sprint) {
        sprintRepository.save(sprint);
    }

    @Override
    public void update(Sprint sprint) {
        sprintRepository.update(sprint);
    }

    @Override
    public void delete(Long id) {
        sprintRepository.deleteById(id);
    }

    @Override
    public Sprint getById(Long id) {
        return sprintRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sprint not found"));
    }

    @Override
    public List<Sprint> getByProjectId(Long projectId) {
        return sprintRepository.findByProjectId(projectId);
    }

    @Override
    public String getProjectNameBySprintId(Long sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new EntityNotFoundException("Sprint not found"));
        Project project = projectService.getById(sprint.getProjectId());
        if (project == null) {
            throw new EntityNotFoundException("Project not found");
        }
        return project.getName();
    }

    @Override
    public List<Sprint> getAllSprints() {
        return sprintRepository.findAll();
    }

    @Override
    public List<Sprint> getAll() {
        return sprintRepository.findAll();
    }
}
