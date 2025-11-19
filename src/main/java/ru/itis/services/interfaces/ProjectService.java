package ru.itis.services.interfaces;

import ru.itis.dto.project.ProjectRequestDto;
import ru.itis.dto.project.ProjectResponseDto;
import ru.itis.entities.Project;

import java.util.List;

public interface ProjectService {
    void create(Project project);
    Project getById(Long id);
    List<Project> getAll();
    void update(Project updated);
    void delete(Long id);

    void create(ProjectRequestDto dto);

    void update(Long id, ProjectRequestDto dto);

    List<ProjectResponseDto> getAllWithManagerName();

    ProjectResponseDto toResponseDtoWithManager(Project project);
}
