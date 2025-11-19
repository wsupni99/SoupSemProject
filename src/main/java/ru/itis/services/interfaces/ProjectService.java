package ru.itis.services.interfaces;

import ru.itis.dto.project.ProjectRequestDto;
import ru.itis.dto.project.ProjectResponseDto;
import ru.itis.entities.Project;

import java.util.List;

public interface ProjectService {
    void create(Project project);
    void create(ProjectRequestDto dto);
    void update(Long id, ProjectRequestDto dto);
    void update(Project updated);
    void delete(Long id);

    Project getById(Long id);
    List<Project> getAll();
    List<ProjectResponseDto> getAllWithManagerName();

    ProjectResponseDto toResponseDtoWithManager(Project project);
}
