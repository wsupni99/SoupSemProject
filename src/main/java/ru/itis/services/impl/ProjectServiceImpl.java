package ru.itis.services.impl;

import ru.itis.dto.project.ProjectRequestDto;
import ru.itis.dto.project.ProjectResponseDto;
import ru.itis.entities.Project;
import ru.itis.entities.User;
import ru.itis.exceptions.EntityNotFoundException;
import ru.itis.exceptions.EntityNotEmptyException;
import ru.itis.mappers.project.ProjectDtoMapper;
import ru.itis.repositories.interfaces.ProjectRepository;
import ru.itis.repositories.interfaces.SprintRepository;
import ru.itis.repositories.interfaces.TaskRepository;
import ru.itis.services.interfaces.ProjectService;
import ru.itis.services.interfaces.UserService;

import java.util.List;
import java.util.stream.Collectors;

public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final SprintRepository sprintRepository;
    private final TaskRepository taskRepository;
    private final UserService userService;

    public ProjectServiceImpl(ProjectRepository projectRepository, SprintRepository sprintRepository, TaskRepository taskRepository, UserService userService) {
        this.projectRepository = projectRepository;
        this.sprintRepository = sprintRepository;
        this.taskRepository = taskRepository;
        this.userService = userService;
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
    public void create(Project project) {
        projectRepository.save(project);
    }

    @Override
    public void update(Project updated) {
        Project existing = projectRepository.findById(updated.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        existing.setStatus(updated.getStatus());
        existing.setManagerId(updated.getManagerId());
        projectRepository.update(existing);
    }

    @Override
    public void delete(Long projectId) {
        if (sprintRepository.countByProjectId(projectId) > 0 ||
                taskRepository.countByProjectId(projectId) > 0) {
            throw new EntityNotEmptyException("You can't delete a project: sprints or tasks are linked to it.");
        }
        projectRepository.deleteById(projectId);
    }

    @Override
    public void create(ProjectRequestDto dto) {
        Project project = ProjectDtoMapper.toEntity(dto);
        projectRepository.save(project);
    }

    @Override
    public void update(Long id, ProjectRequestDto dto) {
        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Проект не найден"));
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());
        existing.setStatus(dto.getStatus());
        existing.setManagerId(dto.getManagerId());
        projectRepository.update(existing);
    }


    @Override
    public List<ProjectResponseDto> getAllWithManagerName() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(this::toResponseDtoWithManager)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectResponseDto toResponseDtoWithManager(Project project) {
        User manager = null;
        if (project.getManagerId() != null) {
            manager = userService.getUserById(project.getManagerId())
                    .orElse(null);
        }
        return ProjectDtoMapper.toResponseDto(project, manager);
    }
}

